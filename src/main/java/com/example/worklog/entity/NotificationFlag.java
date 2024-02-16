package com.example.worklog.entity;

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
@RedisHash(value = "flag",  timeToLive = 1000L)
public class NotificationFlag {

    @Id
    private Long userId;

    @TimeToLive
    private Long ttl;

    public void updateTtl(Long ttl) {
        this.ttl = ttl;
    }
}
