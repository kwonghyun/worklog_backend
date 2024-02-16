package com.example.worklog.dto.calendar;

import com.example.worklog.utils.ValidationConstant;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DayRequestDto {
    @Min(value = 1000, message = ValidationConstant.YEAR_NOT_VALID_MESSAGE)
    @Max(value = 9999, message = ValidationConstant.YEAR_NOT_VALID_MESSAGE)
    @NotNull(message = ValidationConstant.YEAR_NOT_VALID_MESSAGE)
    private Integer year;

    @Min(value = 1, message = ValidationConstant.MONTH_NOT_VALID_MESSAGE)
    @Max(value = 12, message = ValidationConstant.MONTH_NOT_VALID_MESSAGE)
    @NotNull(message = ValidationConstant.MONTH_NOT_VALID_MESSAGE)
    private Integer month;
}
