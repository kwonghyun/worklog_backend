package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class WorkSearchRepoParamDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String keyword;
    private WorkState state;
    private Category category;

    public static WorkSearchRepoParamDto from(
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
        dto.setCategory(requestParamDto.getCategory());
        dto.setState(requestParamDto.getState());
        return dto;
    }
}
