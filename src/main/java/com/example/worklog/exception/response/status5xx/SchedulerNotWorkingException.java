package com.example.worklog.exception.response.status5xx;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import org.springframework.http.HttpStatus;

public class SchedulerNotWorkingException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public SchedulerNotWorkingException() {
        super(ErrorMessage.SCHEDULER_FAILED);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
