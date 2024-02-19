package com.example.worklog.repository.querydsl;

import com.example.worklog.dto.CustomPage;
import com.example.worklog.dto.CustomPageable;
import com.example.worklog.dto.work.WorkSearchServiceDto;
import com.example.worklog.entity.QWork;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
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
public class WorkRepositoryCustomImpl implements WorkRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QWork qWork = QWork.work;

    @Override
    public CustomPage<Work> findBySearchParams(WorkSearchServiceDto dto, CustomPageable pageable, Long userId) {

        JPAQuery<Work> selectFromWhere = queryFactory.selectFrom(qWork)
                .where(
                        qWork.user.id.eq(userId),
                        startDateGoe(dto.getStartDate()),
                        endDateLoe(dto.getEndDate()),
                        keywordLike(dto.getKeyword()),
                        categoryEq(dto.getCategory()),
                        stateEq(dto.getState())
                );

        List<Work> works = selectFromWhere
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qWork.date.asc(), qWork.displayOrder.asc())
                .fetch();

        Long count = selectFromWhere.fetchCount();

        return new CustomPage<Work>(works, pageable, count);
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
