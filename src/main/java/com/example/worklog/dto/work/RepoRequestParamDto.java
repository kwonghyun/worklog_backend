package com.example.worklog.dto.work;

import com.example.worklog.dto.GetRequestParamDto;
import com.example.worklog.dto.enums.SortParam;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RepoRequestParamDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private SortParam.Direction direction;
    private SortParam.SortBy sortBy;
    private String keyword;

    public static RepoRequestParamDto fromGetRequestDto(
            GetRequestParamDto requestParamDto
    ) {
        RepoRequestParamDto dto = new RepoRequestParamDto();
        dto.setStartDate(LocalDate.parse(requestParamDto.getStartDate()));
        dto.setEndDate(LocalDate.parse(requestParamDto.getEndDate()));

        return dto;
    }
}
