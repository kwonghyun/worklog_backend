package com.example.worklog.dto.memo;

import com.example.worklog.exception.CustomDateValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemoPostDto {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @CustomDateValidation
    @NotNull(message = "날짜를 입력해주세요")
    private String  date;
}
