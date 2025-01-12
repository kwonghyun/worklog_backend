package com.example.worklog.dto.sseevent;

import com.example.worklog.entity.enums.EventType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ConnectionMessageRes implements SseMessageRes {
    @JsonIgnore
    private Long userId;
    @Builder.Default
    private EventType eventType = EventType.CONNECTION;
    @Override
    public Long getEventId() {
        return userId;
    }
}
