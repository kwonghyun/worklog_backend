package com.example.worklog.dto;

import com.example.worklog.dto.enums.SortParam;
import com.example.worklog.exception.CustomDateValid;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class GetRequestParamDto {

    private int pageNum;

    private int pageSize;

    @CustomDateValid
    private String startDate;

    @CustomDateValid
    private String endDate;

    private SortParam.Direction direction;

    private SortParam.SortBy sortBy;

    private String keyword;

    public GetRequestParamDto(Integer pageNum,
                              Integer pageSize,
                              String startDate,
                              String endDate,
                              SortParam.SortBy sortBy,
                              SortParam.Direction direction,
                              String keyword) {
        this.pageNum = pageNum == null ? 0 : pageNum;
        this.pageSize = pageSize == null ? 10 : pageSize;
        this.sortBy = sortBy == null ? SortParam.SortBy.ID : sortBy;
        this.direction = direction == null ? SortParam.Direction.DESC : direction;
        this.keyword = keyword;
        if (startDate == null && endDate ==null) {
            this.startDate = getToday();
            this.endDate = getToday();
        } else {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    private String getToday() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

}