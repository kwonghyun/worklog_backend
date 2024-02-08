package com.example.worklog.dto.memo;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemoDisplayOrderPatchDto {
    @PositiveOrZero
    private Integer order;
}
