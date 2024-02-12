package com.example.worklog.repository.querydsl;



public interface NotificationRepositoryCustom {
    Boolean existsByWorkId(Long workId);
}
