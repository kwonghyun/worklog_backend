package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import com.example.worklog.exception.validation.EnumValueCheck;
import com.example.worklog.utils.Constants;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkCategoryPatchDto {
    @NotNull(message = Constants.CATEGORY_NOT_BLANK)
    @EnumValueCheck(enumClass = Category.class)
    private String category;
}
