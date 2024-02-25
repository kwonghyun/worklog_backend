package com.example.worklog.service;

import com.example.worklog.dto.sseevent.NotificationMessageDto;
import com.example.worklog.entity.Notification;
import com.example.worklog.entity.NotificationFlag;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.NotificationEntityType;
import com.example.worklog.entity.enums.SseRole;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.NotificationFlagRedisRepository;
import com.example.worklog.repository.NotificationRepository;
import com.example.worklog.repository.WorkRepository;
import com.example.worklog.scheduler.NotificationJob;
import com.example.worklog.utils.EmitterKey;
import com.example.worklog.utils.EnvironmentVariable;
import com.example.worklog.utils.StringConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationFlagRedisRepository notificationFlagRedisRepository;
    private final WorkRepository workRepository;
    private final SseService sseService;
    private final Scheduler scheduler;

    @Transactional
    public void checkNotificationAndSend(Long userId) {
        // 알림 보낼 시간이 지났거나 1시간이내에 알림을 보내야하는 work찾기
        List<Work> worksToNotice = workRepository.readWorkByDeadlineBeforeAndUserAndNoticedFalse(
                LocalDateTime.now()
                        .plusHours(EnvironmentVariable.WORK_DEADLINE_TRIGGER_HOURS)
                        .plusMinutes(EnvironmentVariable.SEARCH_FUTURE_NOTIFICATION_MINUTES),
                userId
        );

        // 알림 보낼 시간이 지나 바로 보내야하는 work 필터링
        List<Work> worksToNoticeNow = worksToNotice.stream()
                .filter(work -> isNeededSendingNow(work.getDeadline()))
                .collect(Collectors.toList());
        createNotificationFrom(worksToNoticeNow);

        // 안보낸 모든 알림 찾아서 전송 (이전에 전송 실패한 알림이 있을 수 있으므로...)
        sendAllNotificationsNotChecked(userId);

        // 1시간 이내에 알림을 보내야하는 work들 찾아서 스케줄러에 등록
        List<Work> worksToReserve = worksToNotice.stream()
                .filter(work -> isNeededReservation(work.getDeadline()))
                .collect(Collectors.toList());
        createNotificationFrom(worksToReserve).stream()
                    .forEach(notification -> reserveNotification(notification));
    }

    @Transactional
    public Notification createNotificationFrom(Work work) {
        work.updateNoticed(true);
        workRepository.save(work);
        return notificationRepository.save(Notification.builder()
                .entityType(NotificationEntityType.WORK)
                .entityId(work.getId())
                .timeToSend(work.getDeadline().minusHours(EnvironmentVariable.WORK_DEADLINE_TRIGGER_HOURS))
                .message(work.toTempNotificationMessage())
                .receiver(work.getUser())
                .build());
    }

    @Transactional
    public List<Notification> createNotificationFrom(List<Work> works) {
        if (works.size() == 0) return new ArrayList<Notification>();

        workRepository.saveAll(
                works.stream()
                        .map(work -> {work.updateNoticed(true); return work;})
                        .collect(Collectors.toList())
        );
        return notificationRepository.saveAll(works.stream()
                .map(work -> Notification.builder()
                        .entityType(NotificationEntityType.WORK)
                        .entityId(work.getId())
                        .timeToSend(work.getDeadline().minusHours(EnvironmentVariable.WORK_DEADLINE_TRIGGER_HOURS))
                        .receiver(work.getUser())
                        .message(work.toTempNotificationMessage())
                        .build())
                .collect(Collectors.toList())
        );
    }


    @Transactional
    public void sendAllNotificationsNotChecked(Long userId) {
        // isChecked가 false인 알림 모두 찾기
        List<Notification> notifications = notificationRepository.findAllByUserIdAndIsSentFalse(userId);
        log.info("즉시 알림 보낼 알림 갯수 : {}", notifications.size());
        sendNotification(notifications);
    }

    @Transactional
    public void reserveNotification(Notification notification) {

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("sseService", this.sseService);
        jobDataMap.put("notificationRepository", this.notificationRepository);
        jobDataMap.put("notification", notification);

        JobDetail jobDetail = JobBuilder.newJob(NotificationJob.class)
                .setJobData(jobDataMap)
                .withIdentity("work_" + notification.getEntityId(), "work_notification")
                .build();

        Date startAt = Date.from(
                notification.getTimeToSend()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );

        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(startAt)
                .withIdentity("work_" + notification.getEntityId(), "work_notification")
                .forJob(jobDetail)
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            notification.updateIsSent(true);
            notificationRepository.save(notification);
            log.info("스케줄링 완료 workId: {}, notificationId: {}", notification.getEntityId(), notification.getId());
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.SCHEDULER_FAILED);
        }
    }

    public boolean existsReservedNotification(Work work) {
        Long workId = work.getId();
        // "work_" + workId, "work_notification"
        try {
            return scheduler.checkExists(TriggerKey.triggerKey("work_" + workId, "work_notification"));
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Transactional
    public void cancelReservedNotification(Work work) {
        Long workId = work.getId();
        // "work_" + workId, "work_notification"
        try {
            scheduler.unscheduleJob(TriggerKey.triggerKey("work_" + workId, "work_notification"));
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }
    @Transactional
    public void sendNotification(Notification notification) {
        Long userId = notification.getReceiver().getId();
        notification.updateMessage(
                StringConverter.completeWorkNotificationMessage(notification.getMessage()));
        EmitterKey emitterKey = new EmitterKey(userId, SseRole.NOTIFICATION);
        try {
            sseService.sendToClient(emitterKey, NotificationMessageDto.fromEntity(notification));
            notification.updateIsSent(true);
        } catch (Exception e) {
            notification.updateIsSent(false);
        }
        notificationRepository.save(notification);
    }

    @Transactional
    public void sendNotification(List<Notification> notifications) {
        if (notifications.size() == 0) return;
        notificationRepository.saveAll(
                notifications.stream()
                    .map(notification -> {
                                notification.updateMessage(
                                        StringConverter.completeWorkNotificationMessage(notification.getMessage()));
                                EmitterKey emitterKey = new EmitterKey(notification.getReceiver().getId(), SseRole.NOTIFICATION);
                                sseService.sendToClient(emitterKey, NotificationMessageDto.fromEntity(notification));
                                notification.updateIsSent(true);
                                return notification;
                    })
                    .collect(Collectors.toList())
        );
    }

    public Boolean isNeededSendingNow(LocalDateTime deadline) {
        return deadline.isBefore(
                LocalDateTime.now().plusHours(EnvironmentVariable.WORK_DEADLINE_TRIGGER_HOURS)
        );
    }

    public Boolean isNeededReservation(LocalDateTime deadline) {
        return deadline.isAfter(
                LocalDateTime.now().plusHours(EnvironmentVariable.WORK_DEADLINE_TRIGGER_HOURS)
                )
                && deadline.isBefore(
                        LocalDateTime.now()
                                .plusHours(EnvironmentVariable.WORK_DEADLINE_TRIGGER_HOURS)
                                .plusHours(EnvironmentVariable.SEARCH_FUTURE_NOTIFICATION_MINUTES)
                );
    }

    public Notification findOne(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }

    @Transactional
    public void consumeNotificationFlag(Long userId) {
        if (notificationFlagRedisRepository.existsById(userId)) {
            log.info("NotificationFlagRedisRepository.existsById(userId) : true");
            checkNotificationAndSend(userId);
            notificationFlagRedisRepository.deleteById(userId);
        }
    }

    @Transactional
    public void produceNotificationFlag(Long userId) {
        notificationFlagRedisRepository.save(
                NotificationFlag.builder()
                        .userId(userId)
                        .build());
    }

    public boolean existsByWork(Work work) {
        return notificationRepository.existsByWorkId(work.getId());
    }

    public List<Notification> findAllByWork(Work work) {
        return notificationRepository.findByWorkId(work. getId());
    }
    @Transactional
    public void deleteAll(List<Notification> notifications) {
        notificationRepository.deleteAll(notifications);
    }
}
