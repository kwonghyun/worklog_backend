package com.example.worklog.service;

import com.example.worklog.entity.Notification;
import com.example.worklog.entity.Work;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {
    public void checkNotificationAndSend(Long userId);
    public Notification createNotificationFrom(Work work);
    public List<Notification> createNotificationFrom(List<Work> works);
    public void sendAllNotificationsNotChecked(Long userId);
    public void reserveNotification(Notification notification);
    public boolean existsReservedNotification(Work work);
    public void cancelReservedNotification(Work work);
    public void sendNotification(Notification notification);
    public void sendNotification(List<Notification> notifications);
    public Boolean isNeededSendingNow(LocalDateTime deadline);
    public Boolean isNeededReservation(LocalDateTime deadline);
    public Notification findOne(Long id);
    public void consumeNotificationFlag(Long userId);
    public void produceNotificationFlag(Long userId);
    public boolean existsByWork(Work work);
    public List<Notification> findAllByWork(Work work);
    public void deleteAll(List<Notification> notifications);
}
