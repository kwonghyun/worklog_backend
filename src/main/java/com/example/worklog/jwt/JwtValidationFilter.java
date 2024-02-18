package com.example.worklog.jwt;

import com.example.worklog.entity.RefreshTokenDetails;
import com.example.worklog.entity.User;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.exception.FilterExceptionHandler;
import com.example.worklog.repository.RefreshTokenRedisRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
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
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("request url : {}", request.getServletPath());
        log.info("authHeader 확인: " + authHeader);

        // Header 검증, 비어있지 않고, "Bearer "로 시작하는 경우
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.split(" ")[1];
            // 토큰 유효성 검시
            validateToken(token, response);

            Authentication authentication;
            // reissue 요청일 때
            if (request.getServletPath().equals("/users/reissue")) {
                log.info("redis에서 RefreshTokenDetails 찾아 인증 객체 생성 시작");

                RefreshTokenDetails refreshTokenDetails
                        = getValidatedRefreshTokenDetails(token, request, response);
                User user = refreshTokenDetails.toUser();

                authentication = new CustomAuthenticationToken(user, refreshTokenDetails, user.getAuthorities());
            } else { // 보통 요청일 때
                log.info("access token에서 인증 객체 생성 시작");

                Claims claims = jwtTokenUtils.parseClaims(token);
                User user = jwtTokenUtils.generateUserFromClaims(claims);

                authentication = new CustomAuthenticationToken(user, token, user.getAuthorities());
            }

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            log.info("{} 인증 객체 생성 완료", context.getAuthentication().getName());
        }
        filterChain.doFilter(request, response);
    }

    private void validateToken(String token, HttpServletResponse response) {
        log.info("token 검증 시작: {}", token);
        try {
            jwtTokenUtils.parseClaims(token);
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
    }

    private RefreshTokenDetails getValidatedRefreshTokenDetails(
            String refreshToken,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<RefreshTokenDetails> optionalRefreshTokenDetails
                = refreshTokenRedisRepository.findByRefreshToken(refreshToken);

        if (optionalRefreshTokenDetails.isEmpty()) {
            log.info("redis에 refresh token 없음");
            FilterExceptionHandler.jwtExceptionHandler(response, ErrorCode.WRONG_REFRESH_TOKEN);
        }

        RefreshTokenDetails refreshTokenDetails = optionalRefreshTokenDetails.get();
        if (!IpUtil.getClientIp(request).equals(refreshTokenDetails.getIp())) {
            log.info("refresh token IP 불일치");
            FilterExceptionHandler.jwtExceptionHandler(response, ErrorCode.WRONG_REFRESH_TOKEN);
        }
        return refreshTokenDetails;
    }
}