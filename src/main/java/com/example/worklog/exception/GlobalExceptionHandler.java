package com.example.worklog.exception;

import com.example.worklog.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.worklog.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation 예외 응답
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseDto handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        return ResponseDto.fromValidationException(exception);
    }

    // 커스텀 예외 응답
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity handleCustomException(CustomException ex) {
        return new ResponseEntity(ResponseDto.fromErrorCode(ex.getErrorCode()), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleServerException(Exception ex) {
        return new ResponseEntity(ResponseDto.fromErrorCode(INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
