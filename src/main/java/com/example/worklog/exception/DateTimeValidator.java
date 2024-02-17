package com.example.worklog.exception;

import com.example.worklog.utils.Constants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class DateTimeValidator implements ConstraintValidator<DateTimePattern, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            LocalDateTime.parse(value, Constants.DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            log.error("DateTimeValidator : {}", e.getMessage());
            return false;
        }
        return true;
    }


}