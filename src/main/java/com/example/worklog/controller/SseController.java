package com.example.worklog.controller;

import com.example.worklog.entity.enums.SseRole;
import com.example.worklog.service.SseService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(
            Authentication auth,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")
            String lastEventId,
            HttpServletResponse response
    ) {
        SseEmitter emitter = sseService.subscribe(auth.getName(), SseRole.NOTIFICATION, lastEventId);
        response.setHeader("X-Accel-Buffering", "no");
        return ResponseEntity.ok(emitter);
    }

}