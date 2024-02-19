package com.example.worklog.repository.querydsl;

import com.example.worklog.dto.CustomPage;
import com.example.worklog.dto.CustomPageable;
import com.example.worklog.dto.memo.MemoSearchServiceDto;
import com.example.worklog.entity.Memo;
import com.example.worklog.entity.QMemo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@RequiredArgsConstructor
@Repository
@Slf4j
public class MemoRepositoryCustomImpl implements MemoRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QMemo qMemo = QMemo.memo;
    @Override
    public CustomPage<Memo> findBySearchParams(MemoSearchServiceDto dto, CustomPageable pageable, Long userId) {

        JPAQuery<Memo> selectFromWhere = queryFactory.selectFrom(qMemo)
                .where(
                        qMemo.user.id.eq(userId),
                        startDateGoe(dto.getStartDate()),
                        endDateLoe(dto.getEndDate()),
                        keywordLike(dto.getKeyword())
                );

        List<Memo> memos = selectFromWhere
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qMemo.date.asc(), qMemo.displayOrder.asc())
                .fetch();

        Long count = selectFromWhere.fetchCount();

        return new CustomPage<Memo>(memos, pageable, count);
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
