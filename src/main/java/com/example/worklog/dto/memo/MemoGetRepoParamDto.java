package com.example.worklog.dto.memo;

import com.example.worklog.dto.enums.SortParam;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemoGetRepoParamDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private SortParam.Direction direction;
    private SortParam.SortBy sortBy;
    private String keyword;

    public static MemoGetRepoParamDto fromGetRequestDto(
            MemoGetRequestParamDto requestParamDto
    ) {
        MemoGetRepoParamDto dto = new MemoGetRepoParamDto();
        if (requestParamDto.getStartDate() != null) {
            dto.setStartDate(LocalDate.parse(requestParamDto.getStartDate()));
        }
        if (requestParamDto.getEndDate() != null) {
            dto.setEndDate(LocalDate.parse(requestParamDto.getEndDate()));
        }
        dto.setKeyword(requestParamDto.getKeyword());
        dto.setDirection(requestParamDto.getDirection());
        dto.setSortBy(requestParamDto.getSortBy());
        return dto;
    }
}
