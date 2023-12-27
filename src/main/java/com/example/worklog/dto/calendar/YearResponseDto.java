package com.example.worklog.dto.calendar;

import lombok.Data;

import java.util.List;
@Data
public class YearResponseDto {
    List<Integer> years;

    public static YearResponseDto fromList(List<Integer> years) {
        YearResponseDto dto = new YearResponseDto();
        dto.setYears(years);
        return dto;
    }
}
