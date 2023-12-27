package com.example.worklog.dto;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;

@Data
public class ResourceResponseDto<T> {
    int status = 200;
    int count;
    T data;

    public static <T> ResourceResponseDto<T> fromData(T data, int count) {
        ResourceResponseDto<T> resourceResponseDto = new ResourceResponseDto<>();
        resourceResponseDto.setData(data);
        resourceResponseDto.setCount(count);
        if (count == 0) {
            resourceResponseDto.setStatus(204);
        }
        return resourceResponseDto;
    }
}
