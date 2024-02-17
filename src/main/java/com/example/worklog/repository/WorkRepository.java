package com.example.worklog.repository;

import com.example.worklog.dto.work.WorkSearchServiceDto;
import com.example.worklog.entity.Work;
import com.example.worklog.repository.querydsl.WorkRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long>, WorkRepositoryCustom {

    @Query(
            "SELECT w FROM Work w " +
                    "WHERE " +
                        "(w.date = :date) " +
                        "AND (w.user.id = :userId) " +
                    "ORDER BY w.displayOrder ASC "
    )
    List<Work> readWorksByParamsAndUser(@Param("date") LocalDate date, @Param("userId") Long userId);

//    IS NULL 에 enum이 들어오면
//    java.lang.NullPointerException: Cannot invoke "org.hibernate.metamodel.mapping.JdbcMapping.getJdbcValueBinder()" because "jdbcMapping" is null
//    Querydsl로 동적쿼리 만들 예정
    @Query(
            "SELECT w FROM Work w " +
                    "WHERE " +
                    "(:#{#dto.startDate} IS NULL OR w.date >= :#{#dto.startDate}) " +
                    "AND (:#{#dto.endDate} IS NULL OR w.date <= :#{#dto.endDate}) " +
                    "AND (:#{#dto.keyword} IS NULL OR w.content LIKE :#{#dto.keyword}) " +
                    "AND (:#{#dto.category} IS NULL OR w.category = :#{#dto.category}) " +
                    "AND (:#{#dto.state} IS NULL OR w.state = :#{#dto.state}) " +
                    "AND (w.user.id = :userId) " +
                    "ORDER BY w.date ASC, w.displayOrder ASC "
    )
    Page<Work> searchWorksByParamsAndUser(@Param("dto") WorkSearchServiceDto repoDto, @Param("userId") Long userId, Pageable pageable);

    @Query(
            "SELECT w FROM Work w " +
                    "WHERE (w.deadline <= :deadline) " +
                    "AND (w.state = com.example.worklog.entity.enums.WorkState.IN_PROGRESS) " +
                    "AND (w.noticed = false) " +
                    "AND (w.user.id = :userId) " +
                    "ORDER BY w.deadline ASC "
    )
    List<Work> readWorkByDeadlineBeforeAndUserAndNoticedFalse(@Param("deadline") LocalDateTime deadline, @Param("userId") Long userId);
    @Query(
            "SELECT COUNT(w) FROM Work w " +
                    "WHERE " +
                        "(w.date = :targetDate) " +
                        "AND (w.user.id = :userId) "
    )
    Integer countDisplayOrder(@Param("targetDate") LocalDate targetDate, @Param("userId") Long userId);

    @Query(
            "SELECT w FROM Work w " +
                    "WHERE " +
                        "(w.user.id = :userId) " +
                        "AND (w.date = :targetDate) " +
                        "AND (w.displayOrder >= :smallOrder) " +
                        "AND (w.displayOrder <= :bigOrder) " +
                    "ORDER BY w.displayOrder ASC "
    )
    List<Work> readWorksToUpdateDisplayOrder(
            @Param("targetDate") LocalDate targetDate,
            @Param("userId") Long userId,
            @Param("smallOrder") Integer smallOrder,
            @Param("bigOrder") Integer bigOrder
    );
}
