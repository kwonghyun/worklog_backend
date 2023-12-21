package com.example.worklog.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Importance {
    HIGH, MID, LOW;

    @JsonCreator
    public static Importance from(String value) {
        try {
            return Importance.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
