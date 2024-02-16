package com.example.worklog.dto.work;

import com.example.worklog.utils.ValidationConstant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkDisplayOrderPatchDto {
    @PositiveOrZero(message = ValidationConstant.DISPLAY_ORDER_NOT_VALID)
    @NotNull(message = ValidationConstant.DISPLAY_ORDER_NOT_VALID)
    private Integer order;
}
