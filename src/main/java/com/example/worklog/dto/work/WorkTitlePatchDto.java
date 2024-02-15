package com.example.worklog.dto.work;

import com.example.worklog.utils.Constant;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkTitlePatchDto {
    @NotBlank(message = Constant.TITLE_NOT_BLANK)
    private String title;
}
