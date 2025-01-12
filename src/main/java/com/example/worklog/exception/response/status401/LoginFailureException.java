package com.example.worklog.exception.response.status401;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import org.springframework.http.HttpStatus;

public class LoginFailureException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public LoginFailureException() {
        super(ErrorMessage.LOGIN_FAILED);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
