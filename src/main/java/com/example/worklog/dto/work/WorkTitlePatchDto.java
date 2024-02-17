package com.example.worklog.dto.work;

import com.example.worklog.utils.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkTitlePatchDto {
    @NotBlank(message = Constants.TITLE_NOT_BLANK)
    private String title;
}
