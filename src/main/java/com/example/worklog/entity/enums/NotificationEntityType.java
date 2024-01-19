package com.example.worklog.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NotificationEntityType {
    USER, MEMO, WORK;

    @JsonCreator
    public static NotificationEntityType from(String value) {
        try {
            return NotificationEntityType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
