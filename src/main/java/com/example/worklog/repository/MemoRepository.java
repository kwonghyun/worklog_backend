package com.example.worklog.repository;

import com.example.worklog.dto.memo.MemoGetRepoParamDto;
import com.example.worklog.entity.Memo;
import com.example.worklog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query(
            "SELECT m FROM Memo m " +
                "WHERE " +
                    "(:#{#dto.startDate} IS NULL OR m.date >= :#{#dto.startDate}) " +
                    "AND (:#{#dto.endDate} IS NULL OR m.date <= :#{#dto.endDate}) " +
                    "AND (:#{#dto.keyword} IS NULL OR m.content LIKE :#{#dto.keyword}) " +
                    "AND (m.user = :user) " +
                "ORDER BY m.displayOrder ASC "
    )
    Page<Memo> readMemosByParamsAndUser(@Param("dto") MemoGetRepoParamDto dto, @Param("user") User user, Pageable pageable);

    @Query(
            "SELECT COUNT(m) FROM Memo m " +
                    "WHERE " +
                        "(m.date = :targetDate) " +
                        "AND (m.user = :user)"
    )
    Integer countDisplayOrder(@Param("targetDate") LocalDate targetDate, @Param("user") User user);


    @Query(
            "SELECT m FROM Memo m " +
                    "WHERE " +
                        "(m.user = :user) " +
                        "AND (m.date = :targetDate) " +
                        "AND (m.displayOrder >= :smallOrder) " +
                        "AND (m.displayOrder <= :bigOrder) " +
                    "ORDER BY m.displayOrder ASC "
    )
    List<Memo> readMemosToUpdateDisplayOrder(
            @Param("targetDate") LocalDate targetDate,
            @Param("user") User user,
            @Param("smallOrder") Integer smallOrder,
            @Param("bigOrder") Integer bigOrder
            );

    @Query(
            "SELECT DISTINCT YEAR(m.date) FROM Memo m " +
                    "WHERE (m.user = :user) " +
                    "ORDER BY YEAR(m.date) ASC "
    )
    List<Integer> readDistinctYear(@Param("user") User user);

    @Query(
            "SELECT DISTINCT MONTH(m.date) FROM Memo m " +
                    "WHERE " +
                        "(YEAR(m.date) = :year) " +
                        "AND (m.user = :user) " +
                    "ORDER BY MONTH(m.date) ASC "
    )
    List<Integer> readDistinctMonthsByYear(
            @Param("year") int year,
            @Param("user") User user
    );

    @Query(
            "SELECT DISTINCT DAY(m.date) FROM Memo m " +
                "WHERE " +
                    "(YEAR(m.date) = :year) " +
                    "AND (MONTH(m.date) = :month) " +
                    "AND (m.user = :user) " +
                "ORDER BY DAY(m.date) ASC "
    )
    List<Integer> readDistinctDaysByYearAndMonth(
            @Param("year") int year,
            @Param("month") int month,
            @Param("user") User user
    );
}
