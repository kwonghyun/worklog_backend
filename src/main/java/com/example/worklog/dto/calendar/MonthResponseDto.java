package com.example.worklog.dto.calendar;

import com.example.worklog.dto.ResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class MonthResponseDto {
    List<Integer> months;

    public static MonthResponseDto fromList(List<Integer> months) {
        MonthResponseDto dto = new MonthResponseDto();
        dto.setMonths(months);
        return dto;
    }
}
