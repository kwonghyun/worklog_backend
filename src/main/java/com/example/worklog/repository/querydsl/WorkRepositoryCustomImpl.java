package com.example.worklog.repository.querydsl;

import com.example.worklog.dto.CustomPage;
import com.example.worklog.dto.CustomPageable;
import com.example.worklog.dto.work.WorkSearchReqParam;
import com.example.worklog.entity.QWork;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
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
public class WorkRepositoryCustomImpl implements WorkRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QWork qWork = QWork.work;

    @Override
    public CustomPage<Work> findBySearchParams(WorkSearchReqParam dto, CustomPageable pageable, Long userId) {

        // 조건 정의
        BooleanExpression conditions = qWork.user.id.eq(userId)
                .and(startDateGoe(dto.getStartDate()))
                .and(endDateLoe(dto.getEndDate()))
                .and(keywordLike(dto.getKeyword()))
                .and(categoryEq(dto.getCategory()))
                .and(stateEq(dto.getState()));

        // 데이터 쿼리
        List<Work> works = queryFactory.selectFrom(qWork)
                .where(conditions)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qWork.date.asc(), qWork.displayOrder.asc())
                .fetch();

        // 카운트 쿼리
        Long count = Optional.ofNullable(
                queryFactory.select(qWork.id.count())
                        .from(qWork)
                        .where(conditions)
                        .fetchOne()
                ).orElse(0L);

        return new CustomPage<>(works, pageable, count);
    }

    private BooleanExpression startDateGoe(LocalDate startDate) {
        return startDate != null ? qWork.date.goe(startDate) : null;
    }

    private BooleanExpression endDateLoe(LocalDate endDate) {
        return endDate != null ? qWork.date.loe(endDate) : null;
    }

    private BooleanExpression keywordLike(String keyword) {
        return keyword != null ? qWork.content.contains(keyword)
                .or(qWork.title.contains(keyword)) : null;
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? qWork.category.eq(category) : null;
    }

    private BooleanExpression stateEq(WorkState state) {
        return state != null ? qWork.state.eq(state) : null;
    }
}
