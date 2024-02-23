package com.example.worklog.service;

import com.example.worklog.dto.calendar.*;
import com.example.worklog.repository.CalendarRepository;
import com.example.worklog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarServiceImpl implements CalendarService {
    private final CalendarRepository calendarRepository;
    public YearResponseDto readYears(Long userId) {
        List<Integer> yearsFromMemoAndWork = calendarRepository.readDistinctYear(userId);
        return YearResponseDto.fromList(yearsFromMemoAndWork);
    }

    public MonthResponseDto readMonths(MonthRequestDto dto, Long userId) {
        List<Integer> monthsFromMemoAndWork = calendarRepository.readDistinctMonthsByYear(dto.getYear(), userId);
        return MonthResponseDto.fromList(monthsFromMemoAndWork);
    }

    public DayResponseDto readDays(DayRequestDto dto, Long userId) {
        List<Integer> daysFromMemoAndWork = calendarRepository.readDistinctDaysByYearAndMonth(dto.getYear(), dto.getMonth(), userId);
        return DayResponseDto.fromList(daysFromMemoAndWork);
    }
}
