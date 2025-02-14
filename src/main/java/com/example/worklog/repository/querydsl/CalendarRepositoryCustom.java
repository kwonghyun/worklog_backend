package com.example.worklog.repository.querydsl;

import java.util.List;

public interface CalendarRepositoryCustom {
    List<Integer> readDistinctYear(Long userId);
    List<Integer> readDistinctMonthsByYear(int year, Long userId);
    List<Integer> readDistinctDaysByYearAndMonth(int year, int month, Long userId);
}
