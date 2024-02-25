package com.example.worklog.dto.memo;

import com.example.worklog.exception.validation.DatePattern;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemoSearchParamDto {
    @DatePattern
    private String startDate;
    @DatePattern
    private String endDate;
    private String keyword;

    public MemoSearchParamDto(String start_date, String end_date, String key) {
        this.startDate = start_date;
        this.endDate = end_date;
        this.keyword = key;
    }


}