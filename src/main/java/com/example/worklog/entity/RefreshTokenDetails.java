package com.example.worklog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh",  timeToLive = 1000L)
public class RefreshTokenDetails {

    @Id
    private Long userId;
    private String username;
    private LocalDateTime lastNoticedAt;
    private String ip;
    private String authorities;
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