package com.example.worklog.dto.memo;

import com.example.worklog.exception.CustomDateValid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemoPostDto {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @CustomDateValid
    private String  date;
}
