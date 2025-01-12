package com.example.worklog.repository.querydsl;

import com.example.worklog.dto.CustomPage;
import com.example.worklog.dto.CustomPageable;
import com.example.worklog.dto.memo.MemoSearchReqParam;
import com.example.worklog.entity.Memo;
import com.example.worklog.entity.QMemo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Slf4j
public class MemoRepositoryCustomImpl implements MemoRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QMemo qMemo = QMemo.memo;
    @Override
    public CustomPage<Memo> findBySearchParams(MemoSearchReqParam dto, CustomPageable pageable, Long userId) {

        // 조건 정의
        BooleanExpression conditions = qMemo.user.id.eq(userId)
                .and(startDateGoe(dto.getStartDate()))
                .and(endDateLoe(dto.getEndDate()))
                .and(keywordLike(dto.getKeyword()));

        // 데이터 쿼리
        List<Memo> memos = queryFactory.selectFrom(qMemo)
                .where(conditions)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qMemo.date.asc(), qMemo.displayOrder.asc())
                .fetch();

        // 카운트 쿼리
        long count = Optional.ofNullable(
                queryFactory.select(qMemo.id.count())
                        .from(qMemo)
                        .where(conditions)
                        .fetchOne())
                .orElse(0L);
        return new CustomPage<>(memos, pageable, count);
    }
    private BooleanExpression startDateGoe(LocalDate startDate) {
        return startDate != null ? qMemo.date.goe(startDate) : null;
    }

    private BooleanExpression endDateLoe(LocalDate endDate) {
        return endDate != null ? qMemo.date.loe(endDate) : null;
    }

    private BooleanExpression keywordLike(String keyword) {
        return keyword != null ? qMemo.content.contains(keyword) : null;
    }

}
