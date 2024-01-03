package com.example.worklog.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh",  timeToLive = 1000L)
public class RefreshToken {

    @Id
    private String id;

    private String ip;

    private Collection<? extends GrantedAuthority> authorities;

    @Indexed
    private String refreshToken;

    @TimeToLive
    private Long ttl;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public void updateTtl(Long ttl) {
        this.ttl = ttl;
    }
}