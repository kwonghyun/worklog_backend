package com.example.worklog.dto.work;

import com.example.worklog.utils.Constant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkDisplayOrderPatchDto {
    @PositiveOrZero(message = Constant.DISPLAY_ORDER_NOT_VALID)
    @NotNull(message = Constant.DISPLAY_ORDER_NOT_VALID)
    private Integer order;
}
