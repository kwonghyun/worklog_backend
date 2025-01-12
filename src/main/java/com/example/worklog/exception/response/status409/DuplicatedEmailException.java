package com.example.worklog.exception.response.status409;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import org.springframework.http.HttpStatus;

public class DuplicatedEmailException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.CONFLICT;

    public DuplicatedEmailException() {
        super(ErrorMessage.ALREADY_EXISTED_EMAIL);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
