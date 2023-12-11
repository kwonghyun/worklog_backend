package com.example.worklog.dto;

import lombok.Data;

@Data
public class GetResponseDto<T> {
    int status = 200;
    int count;
    T data;

    public static <T> GetResponseDto<T> getData(T data) {
        GetResponseDto<T> getResponseDto = new GetResponseDto<>();
        getResponseDto.setData(data);

        // 페이지 형식 응답이면 갯수 세서 count 입력
        // 그 외에는 count 1
        if (data instanceof PageDto) {
            PageDto<?> pageDto = (PageDto<?>) data;
            getResponseDto.setCount(pageDto.getContent().size());
            if (getResponseDto.getCount() == 0) {
                getResponseDto.status = 204;
            }
        } else {
            getResponseDto.setCount(1);
        }

        return getResponseDto;
    }
}
