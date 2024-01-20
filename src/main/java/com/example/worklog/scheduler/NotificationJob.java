package com.example.worklog.scheduler;

import com.example.worklog.entity.Notification;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.NotificationEntityType;
import com.example.worklog.service.NotificationService;
import com.example.worklog.service.WorkService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class NotificationJob extends QuartzJobBean {

    private NotificationService notificationService;
    private WorkService workService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ApplicationContext appCtx = (ApplicationContext)context.getJobDetail()
                .getJobDataMap().get("applicationContext");
        notificationService = appCtx.getBean(NotificationService.class);
        workService = appCtx.getBean(WorkService.class);

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        Long workId = dataMap.getLong("workId");
        Long notificationId = dataMap.getLong("notificationId");
        String username = dataMap.getString("username");
        if (workId == -1L && notificationId == -1L) {
            System.out.println("초기화 Job 실행");
        } else {
            Work work = workService.findOne(workId);
            Notification notification = notificationService.findOne(notificationId);

            notification.setMessage(
                    notificationService.generateMessage(NotificationEntityType.WORK, work.getId())
            );

            notificationService.sendNotification(username, notification);
        }
    }
}
