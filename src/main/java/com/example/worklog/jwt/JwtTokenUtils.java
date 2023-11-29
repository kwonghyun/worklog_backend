package com.example.worklog.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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

    public JwtTokenUtils(
            @Value("${jwt.secret}") String jwtSecret, @Value("${jwt.accessExpirationTime}") int accessExpirationTime)
    {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(this.signingKey).build();
        this.accessExpirationTime = accessExpirationTime;
    }

    public String generateToken(UserDetails userDetails) {
        log.info("\"{}\" jwt 발급", userDetails.getUsername());
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        Claims jwtClaims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(accessExpirationTime)));

        return Jwts.builder()
                .setClaims(jwtClaims)
                .claim("authorities", authorities)
                .signWith(signingKey)
                .compact();
    }

    public boolean validate(String token) {
        log.info("jwt validate check");
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("JWT 서명이 잘못되었습니다.");
        } catch (ExpiredJwtException e) {
            log.info("JWT 토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("잘못된 토큰입니다.");
        }

        return false;
    }


    public Claims parseClaims(String token) {
        log.info("jwt parsing : {}", jwtParser.parseClaimsJws(token).getBody());
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }

    // 문자열로 저장된 authorities를 다시 Collection으로 변환
    public Collection<? extends GrantedAuthority> getAuthFromClaims(Claims claims){

    String authoritiesString = (String) claims.get("authorities"); // authorities 정보 가져오기

    return Arrays.stream(authoritiesString.split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

}
