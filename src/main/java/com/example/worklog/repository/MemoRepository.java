package com.example.worklog.repository;

import com.example.worklog.dto.memo.MemoGetRepoParamDto;
import com.example.worklog.dto.memo.MemoSearchRepoParamDto;
import com.example.worklog.entity.Memo;
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
                    "(m.date = :#{#dto.date}) " +
                    "AND (m.user.id = :userId) " +
                "ORDER BY m.displayOrder ASC "
    )
    List<Memo> readMemosByParamsAndUser(@Param("dto") MemoGetRepoParamDto dto, @Param("userId") Long userId);

    @Query(
            "SELECT m FROM Memo m " +
                    "WHERE " +
                    "(:#{#dto.startDate} IS NULL OR m.date >= :#{#dto.startDate}) " +
                    "AND (:#{#dto.endDate} IS NULL OR m.date <= :#{#dto.endDate}) " +
                    "AND (:#{#dto.keyword} IS NULL OR m.content LIKE :#{#dto.keyword}) " +
                    "AND (m.user.id = :userId) " +
                    "ORDER BY m.date ASC, m.displayOrder ASC "
    )
    Page<Memo> searchMemosByParamsAndUser(@Param("dto") MemoSearchRepoParamDto dto, Pageable pageable, @Param("userId") Long userId);

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
