package com.example.worklog.exception.response.status400;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import org.springframework.http.HttpStatus;

public class InvalidMemoOrderException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public InvalidMemoOrderException() {
        super(ErrorMessage.MEMO_ORDER_INVALID);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
