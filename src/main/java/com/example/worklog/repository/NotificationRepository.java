package com.example.worklog.repository;

import com.example.worklog.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(
            "SELECT n FROM Notification n " +
                    "WHERE (n.receiver.username = :username) " +
                    "AND (n.isSent = false) " +
                    "ORDER BY n.createdAt ASC "
    )
    List<Notification> findAllByUsernameAndIsSentFalse(@Param("username") String username);
}
