package com.example.worklog.exception.response.status410;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import org.springframework.http.HttpStatus;

public class SseConnectionBrokenException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.GONE;

    public SseConnectionBrokenException() {
        super(ErrorMessage.SSE_CONNECTION_BROKEN);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
