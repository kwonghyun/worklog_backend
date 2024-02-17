package com.example.worklog.repository;

import com.example.worklog.entity.NotificationFlag;
import org.springframework.data.repository.CrudRepository;

public interface NotificationFlagRedisRepository extends CrudRepository<NotificationFlag, Long> {
}
