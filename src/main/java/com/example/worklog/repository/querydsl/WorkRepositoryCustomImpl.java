package com.example.worklog.repository.querydsl;

import com.example.worklog.dto.work.WorkSearchServiceDto;
import com.example.worklog.entity.QWork;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class WorkRepositoryCustomImpl implements WorkRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Work> findBySearchParams(WorkSearchServiceDto dto, Pageable pageable, Long userId) {
        QWork qWork = QWork.work;
        List<Work> works = queryFactory.selectFrom(qWork)
                .where(
                        qWork.user.id.eq(userId),
                        startDateGoe(dto.getStartDate()),
                        endDateLoe(dto.getEndDate()),
                        keywordLike(dto.getKeyword()),
                        categoryEq(dto.getCategory()),
                        stateEq(dto.getState())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qWork.date.asc(), qWork.displayOrder.asc())
                .fetch();

        JPQLQuery<Work> count = queryFactory.selectFrom(qWork)
                .where(
                        qWork.user.id.eq(userId),
                        startDateGoe(dto.getStartDate()),
                        endDateLoe(dto.getEndDate()),
                        keywordLike(dto.getKeyword()),
                        categoryEq(dto.getCategory()),
                        stateEq(dto.getState())
                );
        return PageableExecutionUtils.getPage(works, pageable,() -> count.fetchCount());
    }

    private BooleanExpression startDateGoe(LocalDate startDate) {
        return startDate != null ? QWork.work.date.goe(startDate) : null;
    }

    private BooleanExpression endDateLoe(LocalDate endDate) {
        return endDate != null ? QWork.work.date.loe(endDate) : null;
    }

    private BooleanExpression keywordLike(String keyword) {
        return keyword != null ? QWork.work.content.contains(keyword)
                .or(QWork.work.title.contains(keyword)) : null;
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? QWork.work.category.eq(category) : null;
    }

    private BooleanExpression stateEq(WorkState state) {
        return state != null ? QWork.work.state.eq(state) : null;
    }
}
