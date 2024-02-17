package com.example.worklog.dto.work;

import com.example.worklog.utils.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkContentPatchDto {
    @NotBlank(message = Constants.CONTENT_NOT_BLANK)
    private String content;
}
