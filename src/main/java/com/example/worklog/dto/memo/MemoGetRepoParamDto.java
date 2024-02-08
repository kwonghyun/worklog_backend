package com.example.worklog.dto.memo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
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
