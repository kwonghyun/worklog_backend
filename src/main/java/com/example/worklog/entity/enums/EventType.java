package com.example.worklog.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EventType {
    CONNECTION, NOTIFICATION;
    @JsonCreator
    public static EventType from(String value) {
        try {
            return EventType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
