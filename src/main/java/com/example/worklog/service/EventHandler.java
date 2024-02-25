package com.example.worklog.service;

import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.SseRole;
import com.example.worklog.utils.SseSubscribeEvent;
import com.example.worklog.utils.WorkCreateEvent;
import com.example.worklog.utils.WorkUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandler {
    private final NotificationService notificationService;
    private final SseService sseService;

    @EventListener
    public void onWorkUpdated(WorkUpdateEvent event) {
        Work work = event.getWork();
        deleteNotificationIfExists(work);
        checkConnectionAndNotice(work);
    }

    @EventListener
    public void onWorkCreated(WorkCreateEvent event) {
        Work work = event.getWork();
        checkConnectionAndNotice(work);
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 이미 한번 커밋된 트랜젝션이라 다시 커밋할 수 없어 새로 트랜젝션 만듬
    public void onSseSubscription(SseSubscribeEvent event) throws InterruptedException {
        Long userId = event.getUserId();
        if (!sseService.isSseConnected(userId, SseRole.NOTIFICATION)) {
            log.info("EventHandler.onSseSubscription: sse연결 없어서 종료");
            return;
        }
        notificationService.consumeNotificationFlag(userId);
    }

    private void deleteNotificationIfExists(Work work) {
        if (work.getIsDeleted() && notificationService.existsByWork(work)) {
            notificationService.deleteAll(
                    notificationService.findAllByWork(work)
            );
        }

        // 해당 work로 만들어진 noticed false인 notification 있는지 확인
        if (notificationService.existsReservedNotification(work)) {
            notificationService.cancelReservedNotification(work);
            log.info("EventHandler.onWorkChanged: 이미 예약된 알림 있어서 알림 취소");
        }
    }

    private void checkConnectionAndNotice(Work work) {
        LocalDateTime deadline = work.getDeadline();
        if (deadline == null) {
            log.info("EventHandler.onWorkChanged: deadline이 null이라서 알림 필요없음.");
        } else if (notificationService.isNeededSendingNow(deadline)) {
            if (!sseService.isSseConnected(work.getUser().getId(), SseRole.NOTIFICATION)) {
                notificationService.produceNotificationFlag(work.getUser().getId());
                log.info("EventHandler.onWorkChanged: sse연결이 없어 notificationFlag 생성");
            } else {
                notificationService.sendNotification(
                        notificationService.createNotificationFrom(work)
                );
                log.info("EventHandler.onWorkChanged: 알림 바로 전송됨.");
            }
        } else if (notificationService.isNeededReservation(deadline)) {
            notificationService.reserveNotification(
                    notificationService.createNotificationFrom(work)
            );
            log.info("EventHandler.onWorkChanged: 알림 예약됨.");
        } else {
            log.info("EventHandler.onWorkChanged: 지금 알림을 전송하거나 예약할 필요없음");
        }
    }

}
