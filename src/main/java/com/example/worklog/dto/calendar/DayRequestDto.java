package com.example.worklog.dto.calendar;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class DayRequestDto {
    private int year;
    private int month;

    public DayRequestDto (
            @Pattern(regexp = "^[0-9]{4}$", message = "년도는 YYYY 형식으로 입력해주세요.")
            @RequestParam(name = "year")
            String year,
            @Pattern(regexp = "^[0-9]{2}$", message = "월은 MM 형식으로 입력해주세요.")
            @RequestParam(name = "month")
            String month
    ) {
        this.year = Integer.parseInt(year);
        this.month = Integer.parseInt(month);
    }
}
