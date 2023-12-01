package com.example.worklog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.example.worklog.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation 예외 메시지 출력
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Map<String, Object> handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("code", 400);

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : exception
                .getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        body.put("message", errors);
        return body;
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity handleCustomException(CustomException ex) {
        return new ResponseEntity(ErrorDto.fromErrorCode(ex.getErrorCode()), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleServerException(Exception ex) {
        return new ResponseEntity(ErrorDto.fromErrorCode(INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
