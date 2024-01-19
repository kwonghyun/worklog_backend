package com.example.worklog.repository;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

@Component
public interface SseEmitterRepository {
    public void put(String username, SseEmitter sseEmitter);
    public Optional<SseEmitter> findByKey(String username_SseRole);
    public List<SseEmitter> findByKeyPrefix(String username);
    public Boolean existsByKey(String username_SseRole);

    public void remove(String username);

    public Integer countAll();
}
