package com.example.worklog.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Category {
    FIX,
    UPDATE,
    REFACTOR,
    CHORE,
    FEAT;

    @JsonCreator
    public static Category from(String value) {
        try {
            return Category.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
