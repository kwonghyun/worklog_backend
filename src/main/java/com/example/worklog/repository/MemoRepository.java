package com.example.worklog.repository;

import com.example.worklog.entity.Memo;
import com.example.worklog.repository.querydsl.MemoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long>, MemoRepositoryCustom {

    @Query(
            "SELECT m FROM Memo m " +
                "WHERE " +
                    "(m.date = :date) " +
                    "AND (m.user.id = :userId) " +
                "ORDER BY m.displayOrder ASC "
    )
    List<Memo> readMemosByParamsAndUser(@Param("date") LocalDate date, @Param("userId") Long userId);

    @Query(
            "SELECT COUNT(m) FROM Memo m " +
                    "WHERE " +
                        "(m.date = :targetDate) " +
                        "AND (m.user.id = :userId)"
    )
    Integer countDisplayOrder(@Param("targetDate") LocalDate targetDate, @Param("userId") Long userId);

    @Query(
            "SELECT m FROM Memo m " +
                    "WHERE " +
                        "(m.user.id = :userId) " +
                        "AND (m.date = :targetDate) " +
                        "AND (m.displayOrder >= :smallOrder) " +
                        "AND (m.displayOrder <= :bigOrder) " +
                    "ORDER BY m.displayOrder ASC "
    )
    List<Memo> readMemosToUpdateDisplayOrder(
            @Param("targetDate") LocalDate targetDate,
            @Param("userId") Long userId,
            @Param("smallOrder") Integer smallOrder,
            @Param("bigOrder") Integer bigOrder
            );

}
