package com.example.worklog.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AuthorityType {
    USER, ADMIN;
    @JsonCreator
    public static AuthorityType from(String value) {
        try {
            return AuthorityType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
