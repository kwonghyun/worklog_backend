package com.example.worklog.jwt;


import com.example.worklog.entity.User;
import com.example.worklog.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
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
    @Getter
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

    public JwtDto generateToken(User user) {
        log.info("{} jwt 발급 시작", user);

        // "yyyy-MM-dd HH:mm:ss"
        String lastNoticedAtStr = user.getLastNoticedAt().format(Constants.DATE_TIME_SEC_FORMAT);
        Claims accessTokenClaims = Jwts.claims()
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(accessExpirationTime)));
        String accessToken = Jwts.builder()
                .setClaims(accessTokenClaims)
                .claim("authorities", user.getStringFromAuthorities())
                .claim("id", String.valueOf(user.getId()))
                .claim("last-noticed-at", lastNoticedAtStr)
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

    public Collection<? extends GrantedAuthority> getGrantedAuthoritiesFromString(String authoritiesString) {
        return Arrays.stream(authoritiesString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
