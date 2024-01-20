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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final SseService sseService;
    private final Scheduler scheduler;

    private final ApplicationContext applicationContext;

    public Boolean checkTimeToNotice(String username) {
        User user = getValidatedUserByUsername(username);
        LocalDateTime lastNoticedAt = user.getLastNoticedAt();
        return lastNoticedAt == null || lastNoticedAt.plusHours(1L).isAfter(LocalDateTime.now());
    }

    public void checkNotificationAndSend(String username) {
        log.info("username: {} Notification 객체 만들기", username);
        // 알림 보낼 시간이 지났거나 1시간이내에 알림을 보내야하는 work찾기
        User user = getValidatedUserByUsername(username);
        List<Work> worksToNotice = workRepository.readWorkByDeadlineBeforeAndUserAndNoticedFalse(
                LocalDateTime.now().plusDays(1L).plusHours(1L), user
        );

        // 알림 보낼 시간이 지나 바로 보내야하는 work 필터링 후 isNoticed true로 변경
        List<Work> worksToNoticeNow = worksToNotice.stream()
                .filter(work -> work.getDeadline().isBefore(LocalDateTime.now().plusDays(1L)))
                .map(work -> {work.updateNoticed(true); return work;})
                .collect(Collectors.toList());

        // work로부터 notification 엔티티 생성 후 db 저장
        List<Notification> newNotifications = worksToNoticeNow.stream()
                .map(
                        work -> Notification.builder()
                                .entityType(NotificationEntityType.WORK)
                                .entityId(work.getId())
                                .receiver(user)
                                .build()
                )
                .collect(Collectors.toList());

        log.info("찾은 work 갯수: {}", worksToNotice.size());
        log.info("찾은 work 갯수: {}", worksToNotice.stream().toString());
        log.info("찾은 work noti로 변환된 갯수: {}", newNotifications.size());
        notificationRepository.saveAll(newNotifications);

        // work isNoticed 변경 내용 db 반영 (notification으로 변환 및 저장이 먼저 완료되어야 work도 반영)
        workRepository.saveAll(worksToNoticeNow);

        // 안보낸 모든 알림 찾아서 전송 (이전에 전송 실패한 알림이 있을 수 있으므로...)
        sendAllNotificationsNotChecked(username);

        // 1시간 이내에 알림을 보내야하는 work들 찾아서 스케줄러에 등록
        List<Work> worksToNoticeLater = worksToNotice.stream()
                .filter(work -> work.getDeadline().isAfter(LocalDateTime.now().plusDays(1L)))
                .collect(Collectors.toList());
        reserveNotification(worksToNoticeLater);

        // 마지막으로 알림 보낸 시간 업데이트
        user.updateLastNoticedAt(LocalDateTime.now());
        userRepository.save(user);
    }


    public void sendAllNotificationsNotChecked(String username) {
        // isChecked가 false인 알림 모두 찾기
        List<Notification> notifications = notificationRepository.findAllByUsernameAndIsCheckedFalse(username);

        // 메세지 입력(메세지에 현재시간 부터 몇 시간 뒤인지 알려주기 때문에 나중에 입력)
        notifications.stream().forEach(
                notification -> notification.setMessage(
                        generateMessage(NotificationEntityType.WORK, notification.getEntityId())
                ));
        sendNotification(username, notifications);
    }
    public void reserveNotification(List<Work> works) {
        for (Work work : works) {
            Long workId = work.getId();
            User user = work.getUser();
            Notification notification = notificationRepository.save(
                    Notification.builder()
                    .entityType(NotificationEntityType.WORK)
                    .entityId(work.getId())
                    .receiver(user)
                    .build()
            );
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("applicationContext", applicationContext);
            jobDataMap.put("workId", workId);
            jobDataMap.put("username", user.getUsername());
            jobDataMap.put("notificationId", notification.getId());

            JobDetail jobDetail = JobBuilder.newJob(NotificationJob.class)
                    .setJobData(jobDataMap)
                    .withIdentity("work_" + workId, "work_notification")
                    .build();

            Date startAt = Date.from(
                    work.getDeadline().minusDays(1L)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
            );

            Trigger trigger = TriggerBuilder.newTrigger()
                    .startAt(startAt)
                    .withIdentity("work_" + workId, "work_notification")
                    .forJob(jobDetail)
                    .build();
            try {
                scheduler.scheduleJob(jobDetail, trigger);
                log.info("스케줄링 완료 workId: {}, notificationId: {}", workId, notification.getId());
            } catch (SchedulerException e) {
                throw new CustomException(ErrorCode.SCHEDULER_FAILED);
            }

            // 스케줄된 작업이 실행되기 전에 알림해야할 work로 조회되지 않기 위함
            work.updateNoticed(true);
            workRepository.save(work);
        }
    }

    public String generateMessage(NotificationEntityType type, Long entityId) {
        String message = null;
        switch (type) {
            case USER, MEMO -> throw new CustomException(ErrorCode.ERROR_NOT_FOUND);
            case WORK -> {
                Work work = workRepository.findById(entityId)
                        .orElseThrow(() -> new CustomException(ErrorCode.WORK_NOT_FOUND));
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
        return message;
    }

    public void sendNotification(String username, Notification notification) {
        String emitterKey = username + "_" + SseRole.NOTIFICATION;
        sseService.sendToClient(emitterKey, NotificationDto.fromEntity(notification));
        notification.check();
        notificationRepository.save(notification);
    }
    public void sendNotification(String username, List<Notification> notifications) {

        String emitterKey = username + "_" + SseRole.NOTIFICATION;

        log.info("전송할 Notification 갯수 : {}", notifications.size());
        notifications.stream()
                .map(notification -> {
                    sseService.sendToClient(emitterKey, NotificationDto.fromEntity(notification));
                    return notification;
                })
                .forEach(notification -> notification.check());
        notificationRepository.saveAll(notifications);
    }

    public Notification findOne(Long id) {
        return notificationRepository.findById(id)
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
