package com.example.worklog.jwt;

import com.example.worklog.dto.user.CustomUserDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor

public class AuthCreationFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        log.info("url : {}", request.getServletPath());

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authHeader 확인: " + authHeader);

        // Header 검증, 비어있지 않고, "Bearer "로 시작하는 경우
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authentication;

            if (request.getServletPath().equals("/users/reissue")) {
                log.info("엑세스 토큰 재발급을 위한 익명 인증 객체 생성");
                authentication =
                        new AnonymousAuthenticationToken(
                                "key",
                                "anonymousUser",
                                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
                        );

            } else {
                log.info("사용자 인증 객체 생성 시작");
                Claims claims = jwtTokenUtils.parseClaims(token);
                authentication = new UsernamePasswordAuthenticationToken(
                        CustomUserDetails.builder()
                                .username(claims.getSubject())
                                .id(Long.parseLong((String) claims.get("id")))
                                .build(),
                        token,
                        jwtTokenUtils.getAuthoritiesFromClaims(claims)
                );

            }
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            log.info("{} 인증 객체 생성 완료", context.getAuthentication().getName());
        }


        filterChain.doFilter(request, response);
    }
}