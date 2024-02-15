package com.example.worklog.dto.memo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class MemoSearchRepoParamDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String keyword;

    public static MemoSearchRepoParamDto from(
            MemoSearchParamDto requestParamDto
    ) {
        MemoSearchRepoParamDto dto = new MemoSearchRepoParamDto();
        if (requestParamDto.getStartDate() != null) {
            dto.setStartDate(LocalDate.parse(requestParamDto.getStartDate()));
        }
        if (requestParamDto.getEndDate() != null) {
            dto.setEndDate(LocalDate.parse(requestParamDto.getEndDate()));
        }
        dto.setKeyword(requestParamDto.getKeyword());
        return dto;
    }
}
