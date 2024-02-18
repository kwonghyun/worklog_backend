package com.example.worklog.jwt;

import com.example.worklog.entity.RefreshTokenDetails;
import com.example.worklog.entity.User;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.exception.FilterExceptionHandler;
import com.example.worklog.repository.RefreshTokenRedisRepository;
import com.example.worklog.utils.Constants;
import com.example.worklog.utils.IpUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // request Header에서 jwt 찾기
        log.info("request url : {}", request.getServletPath());
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authHeader 확인: " + authHeader);

        // Header 검증, 비어있지 않고, "Bearer "로 시작하는 경우
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];

            log.info("token 검증 시작: {}", token);
            Claims claims = null;
            try {
                claims = jwtTokenUtils.parseClaims(token);
            } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
                log.info("JWT 서명이 잘못되었습니다.");
                FilterExceptionHandler.jwtExceptionHandler(response, ErrorCode.TOKEN_INVALID);
            } catch (ExpiredJwtException e) {
                log.info("JWT 토큰이 만료되었습니다.");
                FilterExceptionHandler.jwtExceptionHandler(response, ErrorCode.TOKEN_EXPIRED);
            } catch (UnsupportedJwtException e) {
                log.info("지원되지 않는 토큰입니다.");
                FilterExceptionHandler.jwtExceptionHandler(response, ErrorCode.TOKEN_INVALID);
            } catch (IllegalArgumentException e) {
                log.info("잘못된 토큰입니다.");
                FilterExceptionHandler.jwtExceptionHandler(response, ErrorCode.TOKEN_INVALID);
            }

            log.info("인증 객체 생성 시작");
            Authentication authentication;
            if (request.getServletPath().equals("/users/reissue")) {
                RefreshTokenDetails refreshTokenDetails;
                Optional<RefreshTokenDetails> optionalRefreshTokenDetails
                        = refreshTokenRedisRepository.findByRefreshToken(token);
                if (optionalRefreshTokenDetails.isEmpty()) {
                    log.info("RefreshToken 레디스에 없음");
                    FilterExceptionHandler.jwtExceptionHandler(response, ErrorCode.WRONG_REFRESH_TOKEN);
                } else if (!IpUtil.getClientIp(request)
                        .equals(optionalRefreshTokenDetails.get().getIp())) {
                    log.info("RefreshToken IP 불일치");
                    FilterExceptionHandler.jwtExceptionHandler(response, ErrorCode.WRONG_REFRESH_TOKEN);
                }

                refreshTokenDetails = optionalRefreshTokenDetails.get();
                authentication = new CustomAuthenticationToken(
                        User.builder()
                                .username(refreshTokenDetails.getUsername())
                                .id(refreshTokenDetails.getUserId())
                                .lastNoticedAt(refreshTokenDetails.getLastNoticedAt())
                                .build(),
                        refreshTokenDetails,
                        jwtTokenUtils.getGrantedAuthoritiesFromString(refreshTokenDetails.getAuthorities())
                );
            } else {
                log.info("사용자 인증 객체 생성 시작");
                Object lastNoticedAtClaim = claims.get("last-noticed-at");
                LocalDateTime lastNoticedAt = LocalDateTime.parse(lastNoticedAtClaim.toString(), Constants.DATE_TIME_SEC_FORMAT);

                authentication = new CustomAuthenticationToken(
                        User.builder()
                                .username(claims.getSubject())
                                .id(Long.parseLong((String) claims.get("id")))
                                .lastNoticedAt(lastNoticedAt)
                                .build(),
                        token,
                        jwtTokenUtils.getGrantedAuthoritiesFromString((String) claims.get("authorities"))
                );
            }

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            log.info("{} 인증 객체 생성 완료", context.getAuthentication().getName());
        }
        filterChain.doFilter(request, response);
    }
}