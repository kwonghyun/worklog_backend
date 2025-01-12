package com.example.worklog.exception.response.status403;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WrongRefreshTokenException extends CustomResponseException {
    private final HttpStatus httpStatus = HttpStatus.FORBIDDEN;

    public WrongRefreshTokenException() {
        super(ErrorMessage.WRONG_REFRESH_TOKEN);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
