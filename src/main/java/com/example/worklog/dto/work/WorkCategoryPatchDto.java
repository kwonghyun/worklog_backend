package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkCategoryPatchDto {
    @NotNull(message = "올바른 업무 유형을 입력해주세요.")
    private Category category;
}
