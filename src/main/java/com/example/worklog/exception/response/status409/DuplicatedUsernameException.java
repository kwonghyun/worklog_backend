package com.example.worklog.exception.response.status409;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import org.springframework.http.HttpStatus;

public class DuplicatedUsernameException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.CONFLICT;

    public DuplicatedUsernameException() {
        super(ErrorMessage.ALREADY_EXISTED_USERNAME);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
