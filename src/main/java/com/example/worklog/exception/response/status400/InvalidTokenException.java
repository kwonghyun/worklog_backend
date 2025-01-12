package com.example.worklog.exception.response.status400;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.exception.response.CustomResponseException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends CustomResponseException {
  private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

  public InvalidTokenException() {
        super(ErrorMessage.TOKEN_INVALID);
    }

  @Override
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
