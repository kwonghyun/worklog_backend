package com.example.worklog.entity;

import com.example.worklog.entity.base.BaseTimeEntity;
import com.example.worklog.entity.enums.NotificationEntityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE notification SET is_deleted = TRUE WHERE id = ?")
@Where(clause = "is_deleted = FALSE")
public class Notification extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private NotificationEntityType entityType;
    private Long entityId;
    private String message;
    private LocalDateTime timeToSend;
    @Builder.Default
    private Boolean isSent = false;
    @Builder.Default
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    public void updateMessage(String message) {
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
