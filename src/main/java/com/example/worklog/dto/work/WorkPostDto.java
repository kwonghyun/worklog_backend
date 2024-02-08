package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import com.example.worklog.exception.CustomDateTimeValidation;
import com.example.worklog.exception.CustomDateValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkPostDto {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @CustomDateValidation
    @NotNull(message = "날짜를 입력해주세요")
    private String date;

    @CustomDateTimeValidation
    private String deadline;

    @NotNull(message = "업무 유형을 선택해주세요.")
    private Category category;
}
