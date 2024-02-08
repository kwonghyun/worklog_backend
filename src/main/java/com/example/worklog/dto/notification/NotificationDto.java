package com.example.worklog.dto.notification;

import com.example.worklog.entity.Notification;
import com.example.worklog.entity.enums.EventType;
import com.example.worklog.entity.enums.NotificationEntityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationDto {
    private Long notificationId;
    private String message;
    private NotificationEntityType entityType;
    private Long entityId;
    private String sender;
    private EventType eventType;

    public static NotificationDto fromEntity(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setNotificationId(notification.getId());
        dto.setEntityType(notification.getEntityType());
        dto.setEntityId(notification.getEntityId());
        dto.setMessage(notification.getMessage());
        dto.setEventType(EventType.NOTIFICATION);
        return dto;
    }

}
