package com.example.worklog.jwt;

import com.example.worklog.exception.ErrorCode;
import com.example.worklog.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final ObjectMapper objectMapper;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // request Header에서 jwt 찾기
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authHeader 확인: " + authHeader);

        // Header 검증, 비어있지 않고, "Bearer "로 시작하는 경우
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];

            log.info("token 검증 시작: {}", token);
            try {
                jwtTokenUtils.parseClaims(token);
            } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
                log.info("JWT 서명이 잘못되었습니다.");
                jwtExceptionHandler(response, ErrorCode.TOKEN_INVALID);
            } catch (ExpiredJwtException e) {
                log.info("JWT 토큰이 만료되었습니다.");
                jwtExceptionHandler(response, ErrorCode.TOKEN_EXPIRED);
            } catch (UnsupportedJwtException e) {
                log.info("지원되지 않는 토큰입니다.");
                jwtExceptionHandler(response, ErrorCode.TOKEN_INVALID);
            } catch (IllegalArgumentException e) {
                log.info("잘못된 토큰입니다.");
                jwtExceptionHandler(response, ErrorCode.TOKEN_INVALID);
            }
        }

        filterChain.doFilter(request, response);
    }

    public void jwtExceptionHandler(HttpServletResponse response, ErrorCode error) {
        response.setStatus(error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        log.info("필터 에러 커스텀");
        try {
            objectMapper.writeValue(response.getWriter(), ResponseDto.fromErrorCode(error));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}