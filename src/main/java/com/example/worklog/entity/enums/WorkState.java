package com.example.worklog.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum WorkState {
    IN_PROGRESS, COMPLETED;

    @JsonCreator
    public static WorkState from(String value) {
        try {
            return WorkState.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
