package com.example.worklog.dto.memo;

import com.example.worklog.validation.DatePattern;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
public class MemoSearchReqParam {
    @DatePattern
    private final String startDate;
    @DatePattern
    private final String endDate;
    @Getter
    private final String keyword;

    public MemoSearchReqParam(String start_date, String end_date, String key) {
        this.startDate = start_date;
        this.endDate = end_date;
        this.keyword = key;
    }

    public LocalDate getStartDate() {
        return startDate != null ? LocalDate.parse(startDate) : null;
    }
    public LocalDate getEndDate() {
        return endDate != null ? LocalDate.parse(endDate) : null;
    }
}