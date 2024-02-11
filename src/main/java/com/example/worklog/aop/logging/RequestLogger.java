package com.example.worklog.aop.logging;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@Component
@Getter
@Slf4j
@RequestScope
public class RequestLogger {
    private String uuid;
    @Setter
    private String requestMethod;
    @Setter
    private String requestURL;
    @Setter
    private String username;
    @Setter
    private long startTimeMillis;
    @Setter
    private long endTimeMillis;

    public long getExecutionTime() {
        return endTimeMillis - startTimeMillis;
    }
    @Override
    public String toString() {
        return String.format("[%s][%s%s][%s] ", uuid, requestMethod, requestURL, username);
    }

    @PostConstruct
    public void init() {
        setStartTimeMillis(System.currentTimeMillis());
        uuid = UUID.randomUUID().toString();
        log.info("[{}] request scope bean create", uuid);
    }

    @PreDestroy
    public void close() {
        log.info("[{}] request scope bean close", uuid);
    }
}
