package com.example.worklog.dto.memo;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemoContentPatchDto {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
