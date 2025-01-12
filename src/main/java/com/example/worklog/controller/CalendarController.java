package com.example.worklog.controller;

import com.example.worklog.dto.ErrorMessage;
import com.example.worklog.dto.calendar.*;
import com.example.worklog.entity.User;
import com.example.worklog.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Operation(summary = "유효한 년도 조회", description = "업무 혹은 메모가 존재하는 년도를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = YearRes.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/years")
    public ResponseEntity<YearRes> readYears(@AuthenticationPrincipal User user) {
        YearRes dto = calendarService.readYears(user.getId());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/months")
    public ResponseEntity<MonthRes> readMonths(
            @Valid @ModelAttribute MonthReq requestDto,
            @AuthenticationPrincipal User user
    ) {
        MonthRes responseDto = calendarService.readMonths(requestDto, user.getId());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/days")
    public ResponseEntity<DayRes> readDays(
            @Valid @ModelAttribute DayReq requestDto,
            @AuthenticationPrincipal User user
    ) {
        DayRes responseDto = calendarService.readDays(requestDto, user.getId());
        return ResponseEntity.ok(responseDto);
    }
}
