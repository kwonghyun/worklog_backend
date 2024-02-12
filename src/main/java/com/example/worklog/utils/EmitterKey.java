package com.example.worklog.utils;

import com.example.worklog.entity.enums.SseRole;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class EmitterKey {
    private final Long userId;
    private final SseRole sseRole;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmitterKey that)) return false;
        return Objects.equals(userId, that.userId) && sseRole == that.sseRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, sseRole);
    }

    @Override
    public String toString() {
        return sseRole.name() + "_" + userId;
    }
}
