package com.example.worklog.service;

import com.example.worklog.dto.calendar.*;


public interface CalendarService {
    public YearRes readYears(Long userId);
    public MonthRes readMonths(MonthReq dto, Long userId);
    public DayRes readDays(DayReq dto, Long userId);
}
