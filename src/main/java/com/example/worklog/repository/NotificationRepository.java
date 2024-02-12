package com.example.worklog.repository;

import com.example.worklog.entity.Notification;
import com.example.worklog.repository.querydsl.NotificationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
    @Query(
            "SELECT n FROM Notification n " +
                    "WHERE (n.receiver.id = :userId) " +
                    "AND (n.isSent = false) " +
                    "ORDER BY n.createdAt ASC "
    )
    List<Notification> findAllByUserIdAndIsSentFalse(@Param("userId") Long userId);

    @Query(
            "SELECT n FROM Notification n " +
                    "JOIN FETCH n.receiver " +
                    "WHERE n.id = :id"
    )
    Optional<Notification> findByIdFetchReceiver(@Param("id") Long id);

    @Query(
            "SELECT n FROM Notification n " +
                    "WHERE n.entityType = com.example.worklog.entity.enums.NotificationEntityType.WORK " +
                    "AND n.entityId =:workId"
    )
    List<Notification> findByWorkId(Long workId);
}
