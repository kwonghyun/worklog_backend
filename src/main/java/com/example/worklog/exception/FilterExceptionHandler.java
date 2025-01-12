package com.example.worklog.exception;

import com.example.worklog.exception.response.CustomResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
@Slf4j

public class FilterExceptionHandler {
    public static void jwtExceptionHandler(HttpServletResponse response, CustomResponseException exception) {
        response.setStatus(exception.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), exception.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
