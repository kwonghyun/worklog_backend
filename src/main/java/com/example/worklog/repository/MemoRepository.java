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

public interface MemoRepository extends JpaRepository<Memo, Long> {
    @Query(
            "SELECT m FROM Memo m " +
                "WHERE " +
                    ":#{#dto.startDate} IS NULL OR m.date >= :#{#dto.startDate} " +
                    "AND :#{#dto.endDate} IS NULL OR m.date <= :#{#dto.endDate} " +
                    "AND :#{#dto.keyword} IS NULL OR m.content LIKE :#{#dto.keyword} " +
                    "AND m.user = :user " +
                "ORDER BY m.displayOrder ASC "
    )
    Page<Memo> readMemosByParamsAndUser(@Param("dto") MemoGetRepoParamDto dto, @Param("user") User user, Pageable pageable);

    @Query(
            "SELECT COUNT(m) FROM Memo m " +
                    "WHERE m.date = :targetDate " +
                    "AND m.user = :user"
    )
    Integer countDisplayOrder(@Param("targetDate") LocalDate targetDate, @Param("user") User user);
}
