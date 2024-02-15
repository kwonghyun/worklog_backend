package com.example.worklog.dto.calendar;

import com.example.worklog.utils.Constant;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
@ToString
public class DayRequestDto {
    @Min(value = 1000, message = Constant.YEAR_NOT_VALID_MESSAGE)
    @Max(value = 9999, message = Constant.YEAR_NOT_VALID_MESSAGE)
    @NotNull(message = Constant.YEAR_NOT_VALID_MESSAGE)
    private Integer year;

    @Min(value = 1, message = Constant.MONTH_NOT_VALID_MESSAGE)
    @Max(value = 12, message = Constant.MONTH_NOT_VALID_MESSAGE)
    @NotNull(message = Constant.MONTH_NOT_VALID_MESSAGE)
    private Integer month;
}
