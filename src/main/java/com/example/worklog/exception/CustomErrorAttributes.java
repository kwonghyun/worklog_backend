package com.example.worklog.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

        // ErrorDto 형식으로 반환
        Map<String, Object> customAttributes = new LinkedHashMap<>();
        String code = errorAttributes.get("error").toString().replace(" ", "_").toUpperCase();

        customAttributes.put("status", errorAttributes.get("status"));
        customAttributes.put("code", code);
        customAttributes.put("message", errorAttributes.get("message"));

        return customAttributes;
    }
}