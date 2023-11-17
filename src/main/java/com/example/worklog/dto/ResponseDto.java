package com.example.worklog.dto;

import lombok.Data;

@Data
public class ResponseDto {
    String message;

    public static ResponseDto getMessage(String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(message);
        return responseDto;
    }
}
