package com.example.worklog.dto.calendar;

import lombok.Data;

import java.util.List;

@Data
public class DayResponseDto {
    List<Integer> days;

    public static DayResponseDto fromList(List<Integer> days) {
        DayResponseDto dto = new DayResponseDto();
        dto.setDays(days);
        return dto;
    }
}
