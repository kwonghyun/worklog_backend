package com.example.worklog.dto.work;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkContentPatchDto {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
