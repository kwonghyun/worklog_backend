package com.example.worklog.dto.calendar;


import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class MonthRequestDto {


    private int year;

    public MonthRequestDto (
            @Pattern(regexp = "^[0-9]{4}$", message = "년도는 YYYY 형식으로 입력해주세요.")
            @RequestParam(name = "year")
            String year
    ) {
        this.year = Integer.parseInt(year);
    }
}