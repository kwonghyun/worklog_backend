package com.example.worklog.scheduler;

import com.example.worklog.entity.Notification;
import com.example.worklog.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
public class NotificationJob extends QuartzJobBean {

    private NotificationService notificationService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ApplicationContext appCtx = (ApplicationContext)context.getJobDetail()
                .getJobDataMap().get("applicationContext");
        notificationService = appCtx.getBean(NotificationService.class);

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Long notificationId = dataMap.getLong("notificationId");

        Notification notification = notificationService.findOneWithReceiver(notificationId);
        notificationService.sendNotification(notification);

    }
}
