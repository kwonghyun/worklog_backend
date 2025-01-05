package com.example.worklog.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
@Schema(description = "년도 응답 DTO")
public class YearResponseDto {
    @Schema(defaultValue = "[ 2023 , 2024 ]")
    List<Integer> years;

    public static YearResponseDto fromList(List<Integer> years) {
        YearResponseDto dto = new YearResponseDto();
        dto.setYears(years);
        return dto;
    }
}
