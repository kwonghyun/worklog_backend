package com.example.worklog.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WorkState {
    IN_PROGRESS("in progress"), COMPLETED("completed");


    private final String value;
    WorkState(String value) {
        this.value = value;
    }

    @JsonCreator
    public static WorkState from(String value) {
        for (WorkState status : WorkState.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
