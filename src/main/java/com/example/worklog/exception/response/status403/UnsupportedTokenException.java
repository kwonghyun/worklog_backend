package com.example.worklog.exception.response.status403;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import org.springframework.http.HttpStatus;


public class UnsupportedTokenException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.FORBIDDEN;

    public UnsupportedTokenException() {
        super(ErrorMessage.NOT_SUPPORTED_TOKEN);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
