package com.example.worklog.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    FIX("fix"),
    UPDATE("update"),
    REFACTOR("refactor"),
    CHORE("chore"),
    FEAT("feat");

    private final String value;
    Category(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Category from(String value) {
        for (Category category : Category.values()) {
            if (category.getValue().equalsIgnoreCase(value)) {
                return category;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
