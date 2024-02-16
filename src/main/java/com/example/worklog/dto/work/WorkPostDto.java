package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import com.example.worklog.exception.CustomDateTimeValidation;
import com.example.worklog.exception.CustomDateValidation;
import com.example.worklog.utils.ValidationConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkPostDto {
    @NotBlank(message = ValidationConstant.TITLE_NOT_BLANK)
    private String title;

    @NotBlank(message = ValidationConstant.CONTENT_NOT_BLANK)
    private String content;

    @CustomDateValidation
    @NotNull(message = ValidationConstant.DATE_NOT_VALID_MESSAGE)
    private String date;

    @CustomDateTimeValidation
    private String deadline;

    @NotNull(message = ValidationConstant.CATEGORY_NOT_BLANK)
    private Category category;
}
