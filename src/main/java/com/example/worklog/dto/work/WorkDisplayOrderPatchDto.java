package com.example.worklog.dto.work;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class WorkDisplayOrderPatchDto {
    @PositiveOrZero
    private Integer order;
}
