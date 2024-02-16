package com.example.worklog.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SseSubscribeEvent {
    private Long userId;
}
