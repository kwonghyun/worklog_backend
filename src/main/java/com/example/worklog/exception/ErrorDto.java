package com.example.worklog.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDto {
    private int status;
    private String code;
    private String message;

    public static ErrorDto fromErrorCode(ErrorCode e) {
        return new ErrorDto(e.getStatus(), e.getCode(), e.getMessage());
    }
}
