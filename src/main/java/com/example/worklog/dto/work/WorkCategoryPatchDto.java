package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import com.example.worklog.utils.ValidationConstant;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkCategoryPatchDto {
    @NotNull(message = ValidationConstant.CATEGORY_NOT_BLANK)
    private Category category;
}
