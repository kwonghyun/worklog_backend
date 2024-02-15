package com.example.worklog.exception;

import com.example.worklog.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
@Slf4j

public class FilterExceptionHandler {
    public static void jwtExceptionHandler(HttpServletResponse response, ErrorCode error) {
        response.setStatus(error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), ResponseDto.fromErrorCode(error));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
