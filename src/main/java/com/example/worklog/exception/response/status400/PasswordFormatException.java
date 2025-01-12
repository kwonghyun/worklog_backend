package com.example.worklog.exception.response.status400;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import org.springframework.http.HttpStatus;

public class PasswordFormatException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public PasswordFormatException() {
        super(ErrorMessage.WRONG_PASSWORD_FORMAT);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
