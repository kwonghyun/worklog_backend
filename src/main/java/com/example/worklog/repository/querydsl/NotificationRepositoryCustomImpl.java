package com.example.worklog.repository.querydsl;

import com.example.worklog.entity.QNotification;
import com.example.worklog.entity.enums.NotificationEntityType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public Boolean existsByWorkId(Long workId) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(QNotification.notification)
                .where(
                        QNotification.notification.entityId.eq(workId),
                        QNotification.notification.entityType.eq(NotificationEntityType.WORK)
                )
                .fetchFirst();
        return fetchOne != null;
    }
}
