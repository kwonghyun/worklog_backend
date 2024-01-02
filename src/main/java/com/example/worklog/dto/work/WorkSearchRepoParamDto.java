package com.example.worklog.dto.work;

import com.example.worklog.dto.enums.SortParam;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkSearchRepoParamDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private SortParam.Direction direction;
    private SortParam.SortBy sortBy;
    private String keyword;
    private WorkState state;
    private Category category;

    public static WorkSearchRepoParamDto fromGetRequestDto(
            WorkSearchParamDto requestParamDto
    ) {
        WorkSearchRepoParamDto dto = new WorkSearchRepoParamDto();
        if (requestParamDto.getStartDate() != null) {
            dto.setStartDate(LocalDate.parse(requestParamDto.getStartDate()));
        }
        if (requestParamDto.getEndDate() != null) {
            dto.setEndDate(LocalDate.parse(requestParamDto.getEndDate()));
        }
        dto.setKeyword(requestParamDto.getKeyword());
        dto.setDirection(requestParamDto.getDirection());
        dto.setSortBy(requestParamDto.getSortBy());
        dto.setCategory(requestParamDto.getCategory());
        dto.setState(requestParamDto.getState());
        return dto;
    }
}
