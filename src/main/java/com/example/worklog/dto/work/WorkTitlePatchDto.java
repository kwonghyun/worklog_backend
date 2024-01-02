package com.example.worklog.dto.work;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WorkTitlePatchDto {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
}
