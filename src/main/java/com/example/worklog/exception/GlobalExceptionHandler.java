package com.example.worklog.exception;

import com.example.worklog.dto.SimpleMessageRes;
import com.example.worklog.exception.response.CustomResponseException;
import com.example.worklog.validation.ValidationErrorRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation 예외 응답
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidationErrorRes> handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        ValidationErrorRes response = new ValidationErrorRes();

        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    String field = fieldError.getField();
                    String message = fieldError.getDefaultMessage();
                    String rejectedValue;

                    if (field.toLowerCase().contains("password")) {
                        rejectedValue = "*** masked ***";
                    } else {
                        rejectedValue = Optional.ofNullable(fieldError.getRejectedValue())
                                .orElse("null").toString();
                    }
                    log.error("Received invalid input. field : {}, value: {}, message {}", field, rejectedValue, message);

                    response.addError(field, message);
                });

        return ResponseEntity.badRequest().body(response);
    }

    // 커스텀 예외 응답
    @ExceptionHandler(CustomResponseException.class)
    protected ResponseEntity<ErrorMessageRes> handleCustomException(CustomResponseException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorMessageRes(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorMessageRes> handleServerException(Exception exception) {
        return ResponseEntity.internalServerError()
                .body(new ErrorMessageRes(exception.getMessage()));
    }
}
