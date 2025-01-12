package com.example.worklog.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "년도 응답 DTO")
public record YearRes(
        @Schema(defaultValue = "[ 2023 , 2024 ]")
        List<Integer> years
) { }
