package com.example.worklog.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessDto {
    private int status;
    private String code;
    private String message;

    public static SuccessDto fromSuccessCode(SuccessCode successCode) {
        return new SuccessDto(successCode.getStatus(), successCode.getCode(), successCode.getMessage());
    }
}
