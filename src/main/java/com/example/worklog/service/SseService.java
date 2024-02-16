package com.example.worklog.service;

import com.example.worklog.dto.notification.NotificationDto;
import com.example.worklog.entity.enums.EventType;
import com.example.worklog.entity.enums.SseRole;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.NotificationFlagRedisRepository;
import com.example.worklog.repository.SseEmitterRepository;
import com.example.worklog.utils.EmitterKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseService {

    private static final Long sseTimeout = 3 * 60L * 1000;;

    private final SseEmitterRepository sseEmitterRepository;
    private final ObjectProvider<NotificationService> notificationServiceObjectProvider;
    private final NotificationFlagRedisRepository notificationFlagRedisRepository;

    public Boolean isSseConnected(Long userId, SseRole role) {
        return sseEmitterRepository.existsByKey(new EmitterKey(userId, role));
    }

    public SseEmitter subscribe(Long userId, SseRole role, String lastEventId) {
        SseEmitter emitter = new SseEmitter(sseTimeout);
        EmitterKey emitterKey = new EmitterKey(userId, role);
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            sseEmitterRepository.remove(emitterKey);
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            //만료 시 Repository에서 삭제
            emitter.complete();
        });

        sseEmitterRepository.put(emitterKey, emitter);

        try {
            emitter.send(
                    SseEmitter.event()
                            .id("connect_" + userId)
                            .reconnectTime(sseTimeout)
                            .data(
                                    new HashMap<String, EventType>(){{
                                        put("eventType", EventType.CONNECTION);
                                    }},
                                    MediaType.APPLICATION_JSON
                            )
                            .build()
            );
        } catch (IOException exception) {
            sseEmitterRepository.remove(emitterKey);
            log.info("SSE Exception: {}", exception.getMessage());
            throw new CustomException(ErrorCode.SSE_CONNECTION_BROKEN);
        }

        if (notificationFlagRedisRepository.existsByUserId(userId)) {
            NotificationService notificationService = notificationServiceObjectProvider.getObject();
            notificationService.checkNotificationAndSend(userId);
            notificationFlagRedisRepository.deleteByUserId(userId);
        }
        return emitter;
    }

    public void sendToClient(EmitterKey emitterKey, NotificationDto dto) {
        log.info("전송시 emitter 있나요? {}", sseEmitterRepository.existsByKey(emitterKey));
        log.info("emitter 몇개 있나요? {}", sseEmitterRepository.countAll());
        SseEmitter emitter = sseEmitterRepository.findByKey(emitterKey)
                .orElseThrow(() -> new CustomException(ErrorCode.SSE_CONNECTION_BROKEN));
        try {
            emitter.send(
                    SseEmitter.event()
                            .id(emitterKey + "_" + dto.getNotificationId())
                            .reconnectTime(sseTimeout)
                            .data(dto, MediaType.APPLICATION_JSON)
                            .build()
            );
            log.info("SSE : userId: {} 에게 sse message : {} 전송", emitterKey.toString().split("_")[1], dto.toString());
        } catch (IOException exception) {
            sseEmitterRepository.remove(emitterKey);
            log.info("SSE Exception: {}", exception.getMessage());
            throw new CustomException(ErrorCode.SSE_CONNECTION_BROKEN);
        }
    }
}
