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
public class WorkSearchServiceDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private WorkState state;
    private Category category;
    private String keyword;

    public static WorkSearchServiceDto from(
            WorkSearchParamDto requestParamDto
    ) {
        WorkSearchServiceDto dto = new WorkSearchServiceDto();
        if (requestParamDto.getStartDate() != null)
            dto.setStartDate(LocalDate.parse(requestParamDto.getStartDate()));

        if (requestParamDto.getEndDate() != null)
            dto.setEndDate(LocalDate.parse(requestParamDto.getEndDate()));

        if (requestParamDto.getCategory() != null)
            dto.setCategory(Category.from(requestParamDto.getCategory()));

        if (requestParamDto.getState() != null)
            dto.setState(WorkState.from(requestParamDto.getState()));

        dto.setKeyword(requestParamDto.getKeyword());
        return dto;
    }
}
