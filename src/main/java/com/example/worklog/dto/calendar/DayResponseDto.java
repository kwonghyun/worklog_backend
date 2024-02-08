package com.example.worklog.dto.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DayResponseDto {
    List<Integer> days;

    public static DayResponseDto fromList(List<Integer> days) {
        DayResponseDto dto = new DayResponseDto();
        dto.setDays(days);
        return dto;
    }
}
