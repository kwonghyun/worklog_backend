package com.example.worklog.controller;

import com.example.worklog.dto.ResourceResponseDto;
import com.example.worklog.dto.calendar.*;
import com.example.worklog.entity.User;
import com.example.worklog.service.CalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("/years")
    public ResponseEntity<ResourceResponseDto<YearResponseDto>> readYears(@AuthenticationPrincipal User user) {
        YearResponseDto dto = calendarService.readYears(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ResourceResponseDto.fromData(dto, dto.getYears().size()));
    }

    @GetMapping("/months")
    public ResponseEntity<ResourceResponseDto<MonthResponseDto>> readMonths(
            @Valid @ModelAttribute MonthRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        MonthResponseDto responseDto = calendarService.readMonths(requestDto, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ResourceResponseDto.fromData(responseDto, responseDto.getMonths().size()));
    }

    @GetMapping("/days")
    public ResponseEntity<ResourceResponseDto<DayResponseDto>> readDays(
            @Valid @ModelAttribute DayRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        DayResponseDto responseDto = calendarService.readDays(requestDto, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ResourceResponseDto.fromData(responseDto, responseDto.getDays().size()));
    }
}
