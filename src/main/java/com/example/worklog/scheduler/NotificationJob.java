package com.example.worklog.scheduler;

import com.example.worklog.dto.sseevent.NotificationMessageDto;
import com.example.worklog.entity.Notification;
import com.example.worklog.entity.enums.SseRole;
import com.example.worklog.repository.NotificationRepository;
import com.example.worklog.service.NotificationService;
import com.example.worklog.service.SseService;
import com.example.worklog.utils.EmitterKey;
import com.example.worklog.utils.StringConverter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Slf4j
@Component
public class NotificationJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        SseService sseService = (SseService) dataMap.get("sseService");
        NotificationRepository notificationRepository = (NotificationRepository) dataMap.get("notificationRepository");
        Notification notification = (Notification) dataMap.get("notification");

        notification.updateMessage(
                StringConverter.completeWorkNotificationMessage(notification.getMessage())
        );

        Long userId = notification.getReceiver().getId();
        EmitterKey emitterKey = new EmitterKey(userId, SseRole.NOTIFICATION);
        try {
            sseService.sendToClient(emitterKey, NotificationMessageDto.fromEntity(notification));
            log.info("예약된 알림 notificationId: {} 전송됨.", notification.getId());
        } catch (Exception e) {
            Optional<Notification> optionalNotification = notificationRepository.findById(notification.getId());
            if (optionalNotification.isPresent()) {
                Notification notificationToUpdate = optionalNotification.get();
                notificationToUpdate.updateIsSent(false);
                notificationRepository.save(notificationToUpdate);
                log.info("예약된 알림 notificationId: {} 전송 실패. 나중에 다시 시도", notification.getId());
            } else {
                log.info("예약된 알림 notificationId: {} 전송 실패. 삭제된 알림", notification.getId());
            }
        }
    }
}
