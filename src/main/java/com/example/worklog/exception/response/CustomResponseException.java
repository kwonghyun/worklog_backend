package com.example.worklog.exception.response;

import com.example.worklog.dto.ErrorMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomResponseException extends RuntimeException {
    private final ErrorMessage errorMessage;
    public CustomResponseException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public abstract HttpStatus getHttpStatus();
}
