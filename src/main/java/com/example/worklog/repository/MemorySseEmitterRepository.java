package com.example.worklog.repository;

import com.example.worklog.utils.EmitterKey;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MemorySseEmitterRepository implements SseEmitterRepository {
    private final Map<EmitterKey, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    @Override
    public void put(EmitterKey emitterKey, SseEmitter sseEmitter) {
        sseEmitterMap.put(emitterKey, sseEmitter);
    }
    @Override
    public Optional<SseEmitter> findByKey(EmitterKey emitterKey) {
        return Optional.ofNullable(sseEmitterMap.get(emitterKey));
    }


    @Override
    public Boolean existsByKey(EmitterKey emitterKey) {
        return sseEmitterMap.containsKey(emitterKey);
    }

    @Override
    public void remove(EmitterKey emitterKey) {
        sseEmitterMap.remove(emitterKey);
    }

    @Override
    public Integer countAll() {
        return sseEmitterMap.size();
    }
}
