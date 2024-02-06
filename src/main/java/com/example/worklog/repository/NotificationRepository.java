package com.example.worklog.repository;

import com.example.worklog.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(
            "SELECT n FROM Notification n " +
                    "WHERE (n.receiver.username = :username) " +
                    "AND (n.isSent = false) " +
                    "ORDER BY n.createdAt ASC "
    )
    List<Notification> findAllByUsernameAndIsSentFalse(@Param("username") String username);

    @Query(
            "SELECT n FROM Notification n " +
                    "JOIN FETCH n.receiver " +
                    "WHERE n.id = :id"
    )
    Optional<Notification> findByIdFetchReceiver(@Param("id") Long id);
}
