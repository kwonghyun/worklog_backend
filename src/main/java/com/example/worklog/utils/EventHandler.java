package com.example.worklog.utils;

import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.SseRole;
import com.example.worklog.service.NotificationService;
import com.example.worklog.service.SseService;
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
    public void onWorkChanged(WorkChangeEvent workChangeEvent) {
        Work work = workChangeEvent.getWork();
        String username = work.getUser().getUsername();
        if (!sseService.isSseConnected(username, SseRole.NOTIFICATION)) {
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
            notificationService.sendWorkNotification(work);
            log.info("EventHandler.onWorkChanged: 알림 바로 전송됨.");
        } else if (notificationService.isNeededReservation(deadline)) {
            notificationService.reserveNotification(work);
            log.info("EventHandler.onWorkChanged: 알림 예약됨.");
        } else {
            log.info("EventHandler.onWorkChanged: 지금 알림을 전송하거나 예약할 필요없음");
        }
    }
}
