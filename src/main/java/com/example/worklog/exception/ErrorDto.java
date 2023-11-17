package com.example.worklog.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDto {
    private final int status;
    private final String code;
    private final String message;

}
