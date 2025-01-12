package com.example.worklog.exception.response.status404;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SseConnectionNotExistException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public SseConnectionNotExistException() {
        super(ErrorMessage.SSE_CONNECTION_NOT_FOUND);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
