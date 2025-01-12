package com.example.worklog.exception.response.status400;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import org.springframework.http.HttpStatus;

public class EmailFormatException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public EmailFormatException() {
        super(ErrorMessage.WRONG_EMAIL_FORMAT);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
