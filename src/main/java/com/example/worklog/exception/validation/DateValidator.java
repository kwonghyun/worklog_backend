package com.example.worklog.exception.validation;

import com.example.worklog.utils.Constants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
public class DateValidator implements ConstraintValidator<DatePattern, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            LocalDate.parse(value, Constants.DATE_FORMAT);
        } catch (DateTimeParseException e) {
            log.error("DateValidator : {}", e.getMessage());
            return false;
        }
        return true;
    }
}