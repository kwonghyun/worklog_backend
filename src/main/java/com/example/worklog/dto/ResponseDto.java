package com.example.worklog.dto;

import com.example.worklog.exception.ErrorCode;
import com.example.worklog.exception.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ResponseDto<T> {
    private int status;
    private String code;
    private T message;

    // SuccessCode에서 반환
    public static ResponseDto fromSuccessCode(SuccessCode success) {
        return new ResponseDto(success.getStatus(), success.getCode(), success.getMessage());
    }

    // Custom ErroCode에서 변환
    public static ResponseDto fromErrorCode(ErrorCode e) {
        return new ResponseDto(e.getStatus(), e.getCode(), e.getMessage());
    }

    // DefaultErrorAttributes에서 변환
    public static ResponseDto fromErrorAttributes(Map<String, Object> e) {
        return new ResponseDto(
                (Integer) e.get("status"),
                e.get("error").toString().replace(" ", "_").toUpperCase(),
                e.get("message").toString()
        );
    }

    // validation 예외에서 변환
    public static ResponseDto fromValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return new ResponseDto(
                e.getStatusCode().value(),
                e.getStatusCode().toString().substring(4).replace(" ", "_").toUpperCase(),
                errors
        );
    }
}
