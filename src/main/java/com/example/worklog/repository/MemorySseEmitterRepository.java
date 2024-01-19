package com.example.worklog.repository;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class MemorySseEmitterRepository implements SseEmitterRepository {
    private final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    @Override
    public void put(String username_SseRole, SseEmitter sseEmitter) {
        sseEmitterMap.put(username_SseRole, sseEmitter);
    }
    @Override
    public Optional<SseEmitter> findByKey(String username_SseRole) {
        return Optional.ofNullable(sseEmitterMap.get(username_SseRole));
    }

    @Override
    public List<SseEmitter> findByKeyPrefix(String username) {
        return sseEmitterMap.keySet().stream()
                .filter(key -> key.startsWith(username))
                .map(key -> sseEmitterMap.get(key))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existsByKey(String username_SseRole) {
        return sseEmitterMap.containsKey(username_SseRole);
    }

    @Override
    public void remove(String username_SseRole) {
        sseEmitterMap.remove(username_SseRole);
    }

    @Override
    public Integer countAll() {
        return sseEmitterMap.size();
    }
}
