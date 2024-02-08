package com.example.worklog.dto.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class YearResponseDto {
    List<Integer> years;

    public static YearResponseDto fromList(List<Integer> years) {
        YearResponseDto dto = new YearResponseDto();
        dto.setYears(years);
        return dto;
    }
}
