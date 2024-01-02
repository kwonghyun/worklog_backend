package com.example.worklog.dto.memo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MemoGetRepoParamDto {
    private LocalDate date;

    public static MemoGetRepoParamDto fromGetRequestDto(
            MemoGetParamDto requestParamDto
    ) {
        MemoGetRepoParamDto dto = new MemoGetRepoParamDto();
        dto.setDate(LocalDate.parse(requestParamDto.getDate()));
        return dto;
    }
}
