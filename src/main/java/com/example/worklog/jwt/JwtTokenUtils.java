package com.example.worklog.jwt;


import com.example.worklog.dto.user.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JwtTokenUtils {
    private final Key signingKey;
    private final JwtParser jwtParser;
    private final int accessExpirationTime;
    private final int refreshExpirationTime;

    public JwtTokenUtils(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.accessExpirationTime}") int accessExpirationTime,
            @Value("${jwt.refreshExpirationTime}") int refreshExpirationTime
    )
    {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(this.signingKey).build();
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    public JwtDto generateToken(CustomUserDetails userDetails) {
        log.info("\"{}\" jwt 발급", userDetails.getUsername());
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        Claims accessTokenClaims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(accessExpirationTime)));
        String accessToken = Jwts.builder()
                .setClaims(accessTokenClaims)
                .claim("authorities", authorities)
                .claim("id", userDetails.getUserId().toString())
                .signWith(signingKey)
                .compact();

        Claims refreshTokenClaims = Jwts.claims()
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(refreshExpirationTime)));
        String refreshToken = Jwts.builder()
                .setClaims(refreshTokenClaims)
                .signWith(signingKey)
                .compact();

        return JwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Claims parseClaims(String token) {
        log.info("jwt parsing : {}", jwtParser.parseClaimsJws(token).getBody());
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }

    // 문자열로 저장된 authorities를 다시 Collection으로 변환
    public Collection<? extends GrantedAuthority> getAuthoritiesFromClaims(Claims claims){

    String authoritiesString = (String) claims.get("authorities"); // authorities 정보 가져오기

    return Arrays.stream(authoritiesString.split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

}
