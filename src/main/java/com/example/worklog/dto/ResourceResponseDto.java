package com.example.worklog.dto;

import lombok.Data;

@Data
public class ResourceResponseDto<T> {
    int status = 200;
    int count;
    T data;

    public static <T> ResourceResponseDto<T> fromData(T data) {
        ResourceResponseDto<T> resourceResponseDto = new ResourceResponseDto<>();
        resourceResponseDto.setData(data);

        // 페이지 형식 응답이면 content 갯수 세서 count 입력
        // 그 외에는 필드 갯수 세서 count 입력
        if (data instanceof PageDto) {
            PageDto<?> pageDto = (PageDto<?>) data;
            resourceResponseDto.setCount(pageDto.getContent().size());
            if (resourceResponseDto.getCount() == 0) {
                resourceResponseDto.status = 204;
            }
        } else {
            int fieldCount  = data.getClass().getDeclaredFields().length;
            resourceResponseDto.setCount(fieldCount);
        }

        return resourceResponseDto;
    }
}
