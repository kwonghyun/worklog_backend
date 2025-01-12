package com.example.worklog.controller;

import com.example.worklog.dto.SimpleMessageRes;
import com.example.worklog.dto.SuccessMessage;
import com.example.worklog.entity.User;
import com.example.worklog.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/trigger")
    public ResponseEntity<SimpleMessageRes> trigger(@AuthenticationPrincipal User user) {
        notificationService.checkNotificationAndSend(user.getId());

        return ResponseEntity.ok(
                SimpleMessageRes.from(SuccessMessage.SUCCESS));
    }
}
