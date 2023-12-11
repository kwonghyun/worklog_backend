package com.example.worklog.exception;

import com.example.worklog.dto.ResponseDto;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {

        ResponseDto responseDto = ResponseDto.fromErrorAttributes(
                super.getErrorAttributes(webRequest, options)
        );
        return BeanMap.create(responseDto);
    }
}