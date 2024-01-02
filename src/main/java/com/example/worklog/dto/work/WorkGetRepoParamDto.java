package com.example.worklog.dto.work;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkGetRepoParamDto {
    private LocalDate date;

    public static WorkGetRepoParamDto fromGetRequestDto(
            WorkGetParamDto requestParamDto
    ) {
        WorkGetRepoParamDto dto = new WorkGetRepoParamDto();
        dto.setDate(LocalDate.parse(requestParamDto.getDate()));
        return dto;
    }
}
