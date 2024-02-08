package com.example.worklog.service;

import com.example.worklog.dto.notification.NotificationDto;
import com.example.worklog.entity.Notification;
import com.example.worklog.entity.User;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.NotificationEntityType;
import com.example.worklog.entity.enums.SseRole;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.NotificationRepository;
import com.example.worklog.repository.UserRepository;
import com.example.worklog.repository.WorkRepository;
import com.example.worklog.scheduler.NotificationJob;
import com.example.worklog.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final SseService sseService;
    private final Scheduler scheduler;
    private final long accessExpirationTime;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            WorkRepository workRepository,
            SseService sseService,
            Scheduler scheduler,
            @Value("${jwt.accessExpirationTime}")
            long accessExpirationTime
            ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.workRepository = workRepository;
        this.sseService = sseService;
        this.scheduler = scheduler;
        this.accessExpirationTime = accessExpirationTime;
    }

    public Boolean isTimeToNotice(String username) {
        User user = getValidatedUserByUsername(username);
        LocalDateTime lastNoticedAt = user.getLastNoticedAt();
        return lastNoticedAt == null
                || lastNoticedAt
                    .plusHours(Constant.SEARCH_FUTURE_NOTIFICATION_MINUTES)
                    .minusSeconds(accessExpirationTime)
                    .isAfter(LocalDateTime.now());
    }

    public void checkNotificationAndSend(String username) {
        // 알림 보낼 시간이 지났거나 1시간이내에 알림을 보내야하는 work찾기
        User user = getValidatedUserByUsername(username);
        List<Work> worksToNotice = workRepository.readWorkByDeadlineBeforeAndUserAndNoticedFalse(
                LocalDateTime.now().plusHours(Constant.WORK_DEADLINE_TRIGGER_HOURS).plusMinutes(Constant.SEARCH_FUTURE_NOTIFICATION_MINUTES), user
        );

        // 알림 보낼 시간이 지나 바로 보내야하는 work 필터링
        List<Work> worksToNoticeNow = worksToNotice.stream()
                .filter(work -> isNeededSendingNow(work.getDeadline()))
                .collect(Collectors.toList());
        createNotificationFrom(worksToNoticeNow);


        // 안보낸 모든 알림 찾아서 전송 (이전에 전송 실패한 알림이 있을 수 있으므로...)
        sendAllNotificationsNotChecked(username);

        // 1시간 이내에 알림을 보내야하는 work들 찾아서 스케줄러에 등록
        List<Work> worksToReserve = worksToNotice.stream()
                .filter(work -> isNeededReservation(work.getDeadline()))
                .collect(Collectors.toList());
        createNotificationFrom(worksToReserve).stream()
                    .forEach(notification -> reserveNotification(notification));
        // 마지막으로 알림 보낸 시간 업데이트
        user.updateLastNoticedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public Notification createNotificationFrom(Work work) {
        work.updateNoticed(true);
        workRepository.save(work);
        return notificationRepository.save(Notification.builder()
                .entityType(NotificationEntityType.WORK)
                .entityId(work.getId())
                .timeToSend(work.getDeadline().minusHours(Constant.WORK_DEADLINE_TRIGGER_HOURS))
                .receiver(work.getUser())
                .build());
    }

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
                        .timeToSend(work.getDeadline().minusHours(Constant.WORK_DEADLINE_TRIGGER_HOURS))
                        .receiver(work.getUser())
                        .build())
                .collect(Collectors.toList())
        );
    }


    public void sendAllNotificationsNotChecked(String username) {
        // isChecked가 false인 알림 모두 찾기
        List<Notification> notifications = notificationRepository.findAllByUsernameAndIsSentFalse(username);
        log.info("NotificationService.sendAllNotificationsNotChecked: db에서 찾은 알림보낼 알림 갯수 {}", notifications.size());
        sendNotification(notifications);
    }

    public void reserveNotification(Notification notification) {

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("notificationService", this);
        jobDataMap.put("notificationId", notification.getId());

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
            throw new CustomException(ErrorCode.SCHEDULER_FAILED);
        }
    }

    public void cancelReservedNotification(Work work) {
        Long workId = work.getId();
        // "work_" + workId, "work_notification"
        try {
            scheduler.unscheduleJob(TriggerKey.triggerKey("work_" + workId, "work_notification"));
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.SCHEDULER_FAILED);
        }
    }

    public void generateMessage(Notification notification) {
        String message = null;
        NotificationEntityType type = notification.getEntityType();
        switch (type) {
            case USER, MEMO -> throw new CustomException(ErrorCode.ERROR_NOT_FOUND);
            case WORK -> {
                log.info("NotificationService.generateMessage: 여기서 entityId: {}", notification.getEntityId());
                Work work = workRepository.findById(notification.getEntityId())
                        .orElseThrow(() -> new CustomException(ErrorCode.ERROR_GATEWAY_TIMEOUT));
                String date = work.getDate().toString();
                String title = work.getTitle();
                LocalDateTime deadline = work.getDeadline();
                LocalDateTime now = LocalDateTime.now();
                long minDiff = Math.abs(ChronoUnit.MINUTES.between(deadline, now));
                long hour = minDiff / 60;
                long minute = minDiff % 60;
                String timeDiff = hour == 0
                        ? String.format("%d분", minute) : String.format("%d시간 %d분", hour, minute);
                String isExpired = deadline.isBefore(now) ? "지났습니다." : "남았습니다";
                message = String.format("%s에 작성된 업무 : %s의 마감기한이 %s %s.", date, title, timeDiff, isExpired);
            }
        };
        notification.setMessage(message);
    }

    public void sendNotification(Notification notification) {
        String username = notification.getReceiver().getUsername();
        generateMessage(notification);
        String emitterKey = username + "_" + SseRole.NOTIFICATION;
        try {
            sseService.sendToClient(emitterKey, NotificationDto.fromEntity(notification));
            notification.updateIsSent(true);
        } catch (Exception e) {
            notification.updateIsSent(false);
        }
        notificationRepository.save(notification);
    }

    public void sendNotification(List<Notification> notifications) {
        if (notifications.size() == 0) return;

        notificationRepository.saveAll(
                notifications.stream()
                    .map(notification -> {
                                generateMessage(notification);
                                String emitterKey = notification.getReceiver().getUsername() + "_" + SseRole.NOTIFICATION;
                                sseService.sendToClient(emitterKey, NotificationDto.fromEntity(notification));
                                notification.updateIsSent(true);
                                return notification;
                    })
                    .collect(Collectors.toList())
        );
    }

    public Boolean isNeededSendingNow(LocalDateTime deadline) {
        return deadline.isBefore(
                LocalDateTime.now().plusHours(Constant.WORK_DEADLINE_TRIGGER_HOURS)
        );
    }

    public Boolean isNeededReservation(LocalDateTime deadline) {
        return deadline.isAfter(
                LocalDateTime.now().plusHours(Constant.WORK_DEADLINE_TRIGGER_HOURS)
                )
                && deadline.isBefore(
                        LocalDateTime.now()
                                .plusHours(Constant.WORK_DEADLINE_TRIGGER_HOURS)
                                .plusHours(Constant.SEARCH_FUTURE_NOTIFICATION_MINUTES)
                );
    }

    public Notification findOne(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }

    public Notification findOneWithReceiver(Long id) {
        return notificationRepository.findByIdFetchReceiver(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }

    private User getValidatedUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Notification getValidatedNotificationByUserAndNotificationId(User user, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getReceiver().equals(user)) {
            throw new CustomException(ErrorCode.NOTIFICATION_USER_NOT_MATCHED);
        } else {
            return notification;
        }
    }



}
