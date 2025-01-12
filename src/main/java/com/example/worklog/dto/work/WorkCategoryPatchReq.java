package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import com.example.worklog.utils.Constants;
import com.example.worklog.validation.EnumValueCheck;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkCategoryPatchReq {
    @NotNull(message = Constants.CATEGORY_NOT_BLANK)
    @EnumValueCheck(enumClass = Category.class)
    private String category;
}
