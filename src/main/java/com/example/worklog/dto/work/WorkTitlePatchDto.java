package com.example.worklog.dto.work;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkTitlePatchDto {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
}
