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
    private Boolean isChecked = false;
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
            User receiver,
            User sender
    ) {
        this.id = id;
        this.entityType = entityType;
        this.entityId = entityId;
        this.receiver = receiver;
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void check() {
        this.isChecked = true;
    }
}