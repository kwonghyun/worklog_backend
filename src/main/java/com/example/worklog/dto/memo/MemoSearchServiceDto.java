package com.example.worklog.dto.memo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class MemoSearchServiceDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String keyword;

    public static MemoSearchServiceDto from(
            MemoSearchParamDto requestParamDto
    ) {
        MemoSearchServiceDto dto = new MemoSearchServiceDto();
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
