package com.example.worklog.service;

import com.example.worklog.dto.calendar.*;
import com.example.worklog.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarServiceImpl implements CalendarService {
    private final CalendarRepository calendarRepository;
    public YearRes readYears(Long userId) {
        List<Integer> yearsFromMemoAndWork = calendarRepository.readDistinctYear(userId);
        return new YearRes(yearsFromMemoAndWork);
    }

    public MonthRes readMonths(MonthReq dto, Long userId) {
        List<Integer> monthsFromMemoAndWork = calendarRepository.readDistinctMonthsByYear(dto.getYear(), userId);
        return new MonthRes(monthsFromMemoAndWork);
    }

    public DayRes readDays(DayReq dto, Long userId) {
        List<Integer> daysFromMemoAndWork = calendarRepository.readDistinctDaysByYearAndMonth(dto.getYear(), dto.getMonth(), userId);
        return new DayRes(daysFromMemoAndWork);
    }
}
