package com.example.worklog.service;

import com.example.worklog.dto.calendar.*;


public interface CalendarService {
    public YearResponseDto readYears(Long userId);
    public MonthResponseDto readMonths(MonthRequestDto dto, Long userId);
    public DayResponseDto readDays(DayRequestDto dto, Long userId);
}
