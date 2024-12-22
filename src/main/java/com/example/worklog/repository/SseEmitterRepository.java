package com.example.worklog.repository;

import com.example.worklog.utils.EmitterKey;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

@Component
public interface SseEmitterRepository {
    public void put(EmitterKey emitterKey, SseEmitter sseEmitter);
    public Optional<SseEmitter> findByKey(EmitterKey emitterKey);
    public Boolean existsByKey(EmitterKey emitterKey);

    public void remove(EmitterKey emitterKey);

    public Integer countAll();
}
