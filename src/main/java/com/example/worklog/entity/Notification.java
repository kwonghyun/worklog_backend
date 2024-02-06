package com.example.worklog.entity;

import com.example.worklog.entity.base.BaseTimeEntity;
import com.example.worklog.entity.enums.NotificationEntityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE notification SET is_deleted = TRUE WHERE id = ?")
@Where(clause = "is_deleted = FALSE")
public class Notification extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private NotificationEntityType entityType;
    private Long entityId;
    private String message;
    private LocalDateTime timeToSend;
    private Boolean isSent = false;
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    @Builder // 생성시 message는 입력하지 못하기 위함
    protected Notification(
            Long id,
            NotificationEntityType entityType,
            Long entityId,
            LocalDateTime timeToSend,
            User receiver,
            User sender
    ) {
        this.id = id;
        this.entityType = entityType;
        this.entityId = entityId;
        this.timeToSend = timeToSend;
        this.receiver = receiver;
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void updateIsSent(Boolean isSent) {
        this.isSent = isSent;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", entityType=" + entityType +
                ", entityId=" + entityId +
                ", message='" + message + '\'' +
                ", timeToSend=" + timeToSend +
                ", isSent=" + isSent +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
