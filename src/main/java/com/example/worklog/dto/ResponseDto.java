package com.example.worklog.dto;

import lombok.Data;

@Data
public class ResponseDto<T> {
    int status = 200;
    int count;
    T data;

    public static <T> ResponseDto<T> getData(T data) {
        ResponseDto<T> responseDto = new ResponseDto<>();
        responseDto.setData(data);

        // 페이지 형식 응답이면 갯수 세서 count 입력
        // 그 외에는 count 1
        if (data instanceof PageDto) {
            PageDto<?> pageDto = (PageDto<?>) data;
            responseDto.setCount(pageDto.getContent().size());
        } else {
            responseDto.setCount(1);
        }

        return responseDto;
    }
}
