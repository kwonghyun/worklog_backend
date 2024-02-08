package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.exception.CustomDateTimeValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkPutDto {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @CustomDateTimeValidation
    private String deadline;

    @NotNull(message = "업무 상태를 선택해주세요.")
    private WorkState state;

    @NotNull(message = "업무 유형을 선택해주세요.")
    private Category category;

}
