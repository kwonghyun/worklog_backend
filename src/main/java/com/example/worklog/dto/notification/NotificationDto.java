package com.example.worklog.dto.notification;

import com.example.worklog.entity.Notification;
import com.example.worklog.entity.enums.NotificationEntityType;
import lombok.Data;

@Data
public class NotificationDto {
    private Long notificationId;
    private String message;
    private NotificationEntityType entityType;
    private Long entityId;
    private String sender;

    public static NotificationDto fromEntity(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setNotificationId(notification.getId());
        dto.setEntityType(notification.getEntityType());
        dto.setEntityId(notification.getEntityId());
        dto.setMessage(notification.getMessage());
        return dto;
    }

}
