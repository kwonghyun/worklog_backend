package com.example.worklog.dto.calendar;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@ToString
public class MonthRequestDto {

    @Min(value = 1000, message = "년도는 YYYY 형식으로 입력해주세요.")
    @Max(value = 9999, message = "년도는 YYYY 형식으로 입력해주세요.")
    private int year;

    public MonthRequestDto (
            @RequestParam(name = "year")
            int year
    ) {
        this.year = year;
    }
}