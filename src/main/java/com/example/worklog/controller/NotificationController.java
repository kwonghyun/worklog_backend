package com.example.worklog.controller;

import com.example.worklog.dto.ResourceResponseDto;
import com.example.worklog.dto.ResponseDto;
import com.example.worklog.dto.user.CustomUserDetails;
import com.example.worklog.exception.SuccessCode;
import com.example.worklog.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/trigger")
    public ResponseEntity<ResponseDto> trigger(@AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.checkNotificationAndSend(userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.fromSuccessCode(SuccessCode.SUCCESS));
    }

    @GetMapping("/noticed-at/check")
    public ResponseEntity<ResourceResponseDto> check(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Boolean isTimeToNotice = notificationService.isTimeToNotice(userDetails.getLastNoticedAt());
        return ResponseEntity.status(HttpStatus.OK).body(ResourceResponseDto.fromData(
                new HashMap<>(){{put("isTimeToNotice", isTimeToNotice);}}, 1));
    }
}
