package com.example.worklog.service;

import com.example.worklog.entity.Notification;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.SseRole;
import com.example.worklog.utils.SseSubscribeEvent;
import com.example.worklog.utils.WorkChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandler {
    private final NotificationService notificationService;
    private final SseService sseService;
    @EventListener
    public void onWorkChanged(WorkChangeEvent event) {
        Work work = event.getWork();
        Long userId = work.getUser().getId();

        if (work.getIsDeleted() && notificationService.existsByWork(work)) {
            notificationService.deleteAll(
                    notificationService.findAllByWork(work)
            );
        }

        if (!sseService.isSseConnected(userId, SseRole.NOTIFICATION)) {
            log.info("EventHandler.onWorkChanged: sse연결 없어서 종료");
            return;
        }
        // 해당 work로 만들어진 noticed false인 notification 있는지 확인
        if (notificationService.existsReservedNotification(work)) {
            notificationService.cancelReservedNotification(work);
            log.info("EventHandler.onWorkChanged: 이미 예약된 알림 있어서 알림 취소");
        }

        LocalDateTime deadline = work.getDeadline();
        if (deadline == null) {
            log.info("EventHandler.onWorkChanged: deadline이 null이라서 알림 필요없음.");
        } else if (notificationService.isNeededSendingNow(deadline)) {
            Notification notification = notificationService.createNotificationFrom(work);
            notificationService.sendNotification(notification);
            log.info("EventHandler.onWorkChanged: 알림 바로 전송됨.");
        } else if (notificationService.isNeededReservation(deadline)) {
            Notification notification = notificationService.createNotificationFrom(work);
            notificationService.reserveNotification(notification);
            log.info("EventHandler.onWorkChanged: 알림 예약됨.");
        } else {
            log.info("EventHandler.onWorkChanged: 지금 알림을 전송하거나 예약할 필요없음");
        }
    }

    @EventListener
    public void onSseSubscription(SseSubscribeEvent event) {
        Long userId = event.getUserId();
        if (!sseService.isSseConnected(userId, SseRole.NOTIFICATION)) {
            log.info("EventHandler.onSseSubscription: sse연결 없어서 종료");
            return;
        }
        notificationService.consumeNotificationFlag(userId);
    }
}
