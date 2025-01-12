package com.example.worklog.exception.response;

import com.example.worklog.dto.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public abstract class CustomResponseException extends RuntimeException {
    public CustomResponseException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }

    public abstract HttpStatus getHttpStatus();
}
