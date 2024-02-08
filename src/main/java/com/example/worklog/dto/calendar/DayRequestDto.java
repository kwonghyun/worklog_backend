package com.example.worklog.dto.calendar;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@ToString
public class DayRequestDto {
    @Min(value = 1000, message = "년도는 YYYY 형식으로 입력해주세요.")
    @Max(value = 9999, message = "년도는 YYYY 형식으로 입력해주세요.")
    private int year;

    @Min(value = 1, message = "월은 1~12 사이의 값을 입력해주세요.")
    @Max(value = 12, message = "월은 1~12 사이의 값을 입력해주세요.")
    private int month;

    public DayRequestDto (
            @RequestParam(name = "year")
            int year,
            @RequestParam(name = "month")
            int month
    ) {
        this.year = year;
        this.month = month;
    }
}
