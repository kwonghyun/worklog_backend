package com.example.worklog.exception.response.status404;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WorkNotExistException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public WorkNotExistException() {
        super(ErrorMessage.WORK_NOT_FOUND);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
