package com.example.worklog.exception.response.status409;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import org.springframework.http.HttpStatus;

public class PasswordReuseException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.CONFLICT;

    public PasswordReuseException() {
        super(ErrorMessage.ALREADY_USED_PASSWORD);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
