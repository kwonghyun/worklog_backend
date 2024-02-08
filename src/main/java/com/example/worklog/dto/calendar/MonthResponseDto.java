package com.example.worklog.dto.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MonthResponseDto {
    List<Integer> months;

    public static MonthResponseDto fromList(List<Integer> months) {
        MonthResponseDto dto = new MonthResponseDto();
        dto.setMonths(months);
        return dto;
    }
}
