package com.example.worklog.dto.work;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
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
