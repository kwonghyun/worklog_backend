package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.exception.DateTimePattern;
import com.example.worklog.exception.EnumValueCheck;
import com.example.worklog.utils.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkPutDto {
    @NotBlank(message = Constants.TITLE_NOT_BLANK)
    private String title;

    @NotBlank(message = Constants.CONTENT_NOT_BLANK)
    private String content;

    @DateTimePattern
    private String deadline;

    @NotNull(message = Constants.WORK_STATE_NOT_BLANK)
    @EnumValueCheck(enumClass = Category.class, message = Constants.CATEGORY_NOT_VALID_MESSAGE)
    private String category;

    @NotNull(message = Constants.CATEGORY_NOT_BLANK)
    @EnumValueCheck(enumClass = WorkState.class, message = Constants.WORK_STATE_NOT_VALID_MESSAGE)
    private String state;

}
