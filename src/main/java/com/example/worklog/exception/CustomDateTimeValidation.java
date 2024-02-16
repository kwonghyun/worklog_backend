package com.example.worklog.exception;

import com.example.worklog.utils.ValidationConstant;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateTimeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomDateTimeValidation {
    String message() default ValidationConstant.DATE_TIME_NOT_VALID_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String pattern() default "yyyy-MM-dd HH:mm";

}
