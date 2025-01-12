package com.example.worklog.exception.response.status403;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import org.springframework.http.HttpStatus;


public class WrongPasswordException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.FORBIDDEN;

    public WrongPasswordException() {
        super(ErrorMessage.WRONG_PASSWORD);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
