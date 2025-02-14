package com.example.worklog.repository.querydsl;

import com.example.worklog.entity.QMemo;
import com.example.worklog.entity.QWork;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class CalendarRepositoryCustomImpl implements CalendarRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QMemo qMemo = QMemo.memo;
    private final QWork qWork = QWork.work;

    @Override
    public List<Integer> readDistinctYear(Long userId) {
        List<Integer> memoYears = queryFactory
                .select(qMemo.date.year())
                .distinct()
                .from(qMemo)
                .where(
                        qMemo.isDeleted.eq(false)
                                .and(qMemo.user.id.eq(userId)))
                .fetch();

        List<Integer> workYears = queryFactory
                .select(qWork.date.year())
                .distinct()
                .from(qWork)
                .where(
                        qWork.isDeleted.eq(false)
                                .and(qWork.user.id.eq(userId)))
                .fetch();

        Set<Integer> years = new LinkedHashSet<>();
        years.addAll(memoYears);
        years.addAll(workYears);
        return years.stream().sorted(Comparator.reverseOrder()).toList();

    }

    @Override
    public List<Integer> readDistinctMonthsByYear(int year, Long userId) {
        List<Integer> memoMonths = queryFactory
                .select(qMemo.date.month())
                .distinct()
                .from(qMemo)
                .where(
                        qMemo.isDeleted.eq(false)
                                .and(qMemo.date.year().eq(year))
                                .and(qMemo.user.id.eq(userId)))
                .fetch();

        List<Integer> workMonths = queryFactory
                .select(qWork.date.month())
                .distinct()
                .from(qWork)
                .where(
                        qWork.isDeleted.eq(false)
                                .and(qWork.date.year().eq(year))
                                .and(qWork.user.id.eq(userId)))
                .fetch();

        Set<Integer> months = new LinkedHashSet<>();
        months.addAll(memoMonths);
        months.addAll(workMonths);
        return months.stream().sorted(Comparator.reverseOrder()).toList();
    }

    @Override
    public List<Integer> readDistinctDaysByYearAndMonth(int year, int month, Long userId) {
        List<Integer> memoDays = queryFactory
                .select(qMemo.date.dayOfMonth())
                .distinct()
                .from(qMemo)
                .where(
                        qMemo.isDeleted.eq(false)
                                .and(qMemo.date.year().eq(year))
                                .and(qMemo.date.month().eq(month))
                                .and(qMemo.user.id.eq(userId)))
                .fetch();

        List<Integer> workDays = queryFactory
                .select(qWork.date.dayOfMonth())
                .distinct()
                .from(qWork)
                .where(
                        qWork.isDeleted.eq(false)
                                .and(qWork.date.year().eq(year))
                                .and(qWork.date.month().eq(month))
                                .and(qWork.user.id.eq(userId)))
                .fetch();

        Set<Integer> days = new LinkedHashSet<>();
        days.addAll(memoDays);
        days.addAll(workDays);
        return days.stream().sorted(Comparator.reverseOrder()).toList();
    }
}
