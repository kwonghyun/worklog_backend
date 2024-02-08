package com.example.worklog.dto.work;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkDisplayOrderPatchDto {
    @PositiveOrZero
    private Integer order;
}
