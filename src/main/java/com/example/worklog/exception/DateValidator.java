package com.example.worklog.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class DateValidator implements ConstraintValidator<CustomDateValidation, String> {

    private String pattern;

    @Override
    public void initialize(CustomDateValidation constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            LocalDate.from(LocalDate.parse(value, DateTimeFormatter.ofPattern(this.pattern)));
        } catch (DateTimeParseException e) {
            log.error("DateValidator : {}", e.getMessage());
            return false;
        }
        return true;
    }
}