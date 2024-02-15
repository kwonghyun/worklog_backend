package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.exception.CustomDateTimeValidation;
import com.example.worklog.utils.Constant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkPutDto {
    @NotBlank(message = Constant.TITLE_NOT_BLANK)
    private String title;

    @NotBlank(message = Constant.CONTENT_NOT_BLANK)
    private String content;

    @CustomDateTimeValidation
    private String deadline;

    @NotNull(message = Constant.WORK_STATE_NOT_BLANK)
    private WorkState state;

    @NotNull(message = Constant.CATEGORY_NOT_BLANK)
    private Category category;

}
