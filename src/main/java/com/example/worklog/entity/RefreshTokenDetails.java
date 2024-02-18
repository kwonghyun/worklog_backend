package com.example.worklog.entity;

import com.example.worklog.entity.enums.AuthorityType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
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

    public User toUser() {
        List<Authority> authorities =
                Arrays.stream(this.authorities.split(","))
                .map(
                        authString -> Authority.builder().authorityType(AuthorityType.from(authString)).build()
                )
                .collect(Collectors.toList());

        return User.builder()
                .id(this.userId)
                .username(this.username)
                .lastNoticedAt(lastNoticedAt)
                .authorities(authorities)
                .build();
    }


}