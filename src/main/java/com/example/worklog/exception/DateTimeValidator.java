package com.example.worklog.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class DateTimeValidator implements ConstraintValidator<CustomDateTimeValidation, String> {

    private String pattern;

    @Override
    public void initialize(CustomDateTimeValidation constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            LocalDateTime.from(LocalDateTime.parse(value, DateTimeFormatter.ofPattern(this.pattern)));
        } catch (DateTimeParseException e) {
            log.error("DateTimeValidator : {}", e.getMessage());
            return false;
        }
        return true;
    }
}