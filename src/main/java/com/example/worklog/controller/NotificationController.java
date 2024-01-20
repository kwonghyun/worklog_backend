package com.example.worklog.controller;

import com.example.worklog.dto.ResourceResponseDto;
import com.example.worklog.dto.ResponseDto;
import com.example.worklog.exception.SuccessCode;
import com.example.worklog.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/trigger")
    public ResponseEntity<ResponseDto> trigger(Authentication auth) {
        notificationService.checkNotificationAndSend(auth.getName());

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.fromSuccessCode(SuccessCode.SUCCESS));
    }

    @GetMapping("/noticed-at/check")
    public ResponseEntity<ResourceResponseDto> check(Authentication auth) {
        Boolean isTimeToNotice = notificationService.checkTimeToNotice(auth.getName());
        Map<String,Boolean> response = new HashMap<>();
        response.put("isTimeToNotice", isTimeToNotice);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceResponseDto.fromData(response, 1));
    }
}