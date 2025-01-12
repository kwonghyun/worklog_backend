package com.example.worklog.dto.sseevent;

import com.example.worklog.entity.Notification;
import com.example.worklog.entity.enums.EventType;
import com.example.worklog.entity.enums.NotificationEntityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class NotificationMessageRes implements SseMessageRes {
    private Long notificationId;
    private String message;
    private NotificationEntityType entityType;
    private Long entityId;
    private String sender;
    private EventType eventType;

    public static NotificationMessageRes from(Notification notification) {
        NotificationMessageRes dto = new NotificationMessageRes();
        dto.setNotificationId(notification.getId());
        dto.setEntityType(notification.getEntityType());
        dto.setEntityId(notification.getEntityId());
        dto.setMessage(notification.getMessage());
        dto.setEventType(EventType.NOTIFICATION);
        return dto;
    }

    @Override
    public Long getEventId() {
        return notificationId;
    }

}
