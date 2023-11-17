package com.example.worklog.jwt;

import com.example.worklog.dto.user.CustomUserDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;

    public JwtTokenFilter(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // request Header에서 jwt 찾기
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        log.info("authHeader 확인: " + authHeader);
        // Header 검증
        // 비어있지 않고, "Bearer "로 시작하는 경우
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];

            log.info("token 검증 시작: {}", token);
            if (jwtTokenUtils.validate(token)) {
                log.info("사용자 인증정보 생성 시작");
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                Claims claims = jwtTokenUtils.parseClaims(token);

                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        CustomUserDetails.builder()
                                .username(claims.getSubject())
                                .build(),
                        token
                );
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
                log.info("{} 인증완료", context.getAuthentication().getName());

            } else {
                log.warn("jwt 검증 실패");
            }
        }

        filterChain.doFilter(request, response);
    }
}