package com.example.worklog.service;

import com.example.worklog.dto.sseevent.SseMessageDto;
import com.example.worklog.entity.enums.SseRole;
import com.example.worklog.utils.EmitterKey;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


public interface SseService {
    public Boolean isSseConnected(Long userId, SseRole role);
    public SseEmitter subscribe(Long userId, SseRole role, String lastEventId);
    public void sendToClient(EmitterKey emitterKey, SseMessageDto event);
}
