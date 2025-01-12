package com.example.worklog.validation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorRes {

    private final List<ValidationError> errors = new ArrayList<>();

    public void addError(String field, String message) {
        this.errors.add(new ValidationError(field, message));
    }

    public record ValidationError(String field, String message) {}
}