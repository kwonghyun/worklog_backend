package com.example.worklog.scheduler;

import com.example.worklog.entity.Notification;
import com.example.worklog.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class NotificationJob extends QuartzJobBean {

    private NotificationService notificationService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        notificationService = (NotificationService) context.getJobDetail().getJobDataMap().get("notificationService");
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Long notificationId = dataMap.getLong("notificationId");

        Notification notification = notificationService.findOneWithReceiver(notificationId);
        notificationService.sendNotification(notification);
    }
}
