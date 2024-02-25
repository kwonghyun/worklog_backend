package com.example.worklog.service;

import com.example.worklog.aop.logging.request.ExcludeAop;
import com.example.worklog.dto.sseevent.ConnectionMessageDto;
import com.example.worklog.dto.sseevent.SseMessageDto;
import com.example.worklog.entity.enums.SseRole;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.SseEmitterRepository;
import com.example.worklog.utils.EmitterKey;
import com.example.worklog.utils.SseSubscribeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@ExcludeAop
public class SseServiceImpl implements SseService {
    private static final Long sseTimeout = 3 * 60L * 1000;;

    private final SseEmitterRepository sseEmitterRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Boolean isSseConnected(Long userId, SseRole role) {
        return sseEmitterRepository.existsByKey(new EmitterKey(userId, role));
    }
    @Transactional
    public SseEmitter subscribe(Long userId, SseRole role, String lastEventId) {
        SseEmitter emitter = new SseEmitter(sseTimeout);
        EmitterKey emitterKey = new EmitterKey(userId, role);

        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            sseEmitterRepository.remove(emitterKey);
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        sseEmitterRepository.put(emitterKey, emitter);
        sendToClient(emitterKey, ConnectionMessageDto.builder().userId(userId).build());
        log.info("SSE : userId={}에게 연결", userId);

        applicationEventPublisher.publishEvent(
                SseSubscribeEvent.builder().userId(userId).build()
        );

        return emitter;
    }

    @Transactional
    public void sendToClient(EmitterKey emitterKey, SseMessageDto event) {
        SseEmitter emitter = sseEmitterRepository.findByKey(emitterKey)
                .orElseThrow(() -> new CustomException(ErrorCode.SSE_CONNECTION_BROKEN));
        try {
            emitter.send(
                    SseEmitter.event()
                            .id(emitterKey + "_" + event.getEventId())
                            .reconnectTime(sseTimeout)
                            .data(event, MediaType.APPLICATION_JSON)
                            .build()
            );
            log.info("SSE : userId={} 에게 message 전송 {}", emitterKey.toString().split("_")[1], event);
        } catch (IOException exception) {
            sseEmitterRepository.remove(emitterKey);
            log.info("SSE Exception : {}", exception.getMessage());
            throw new CustomException(ErrorCode.SSE_CONNECTION_BROKEN);
        }
    }
}
