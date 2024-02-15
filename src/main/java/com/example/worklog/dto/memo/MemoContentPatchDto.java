package com.example.worklog.dto.memo;

import com.example.worklog.utils.Constant;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemoContentPatchDto {
    @NotBlank(message = Constant.CONTENT_NOT_BLANK)
    private String content;
}
