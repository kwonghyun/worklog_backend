package com.example.worklog.dto.memo;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class MemoDisplayOrderPatchDto {
    @PositiveOrZero
    private Integer order;
}
