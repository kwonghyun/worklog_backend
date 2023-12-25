package com.example.worklog.repository;

import com.example.worklog.dto.work.WorkGetRepoParamDto;
import com.example.worklog.entity.Memo;
import com.example.worklog.entity.User;
import com.example.worklog.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    @Query(
            "SELECT w FROM Work w " +
                    "WHERE " +
                        "(:#{#dto.startDate} IS NULL OR w.date >= :#{#dto.startDate}) " +
                        "AND (:#{#dto.endDate} IS NULL OR w.date <= :#{#dto.endDate}) " +
                        "AND (:#{#dto.keyword} IS NULL OR w.content LIKE :#{#dto.keyword}) " +
                        "AND (:#{#dto.category} IS NULL OR w.category = :#{#dto.category}) " +
                        "AND (:#{#dto.state} IS NULL OR w.state = :#{#dto.state}) " +
                        "AND (w.user = :user) " +
                    "ORDER BY w.displayOrder ASC"
    )
    Page<Work> readWorksByParamsAndUser(@Param("dto") WorkGetRepoParamDto repoDto, @Param("user") User user, Pageable pageable);

    @Query(
            "SELECT COUNT(w) FROM Work w " +
                    "WHERE " +
                        "(w.date = :targetDate) " +
                        "AND (w.user = :user)"
    )
    Integer countDisplayOrder(@Param("targetDate") LocalDate targetDate, @Param("user") User user);

    @Query(
            "SELECT w FROM Work w " +
                    "WHERE " +
                    "(w.user = :user) " +
                    "AND (w.date = :targetDate) " +
                    "AND (w.displayOrder >= :smallOrder) " +
                    "AND (w.displayOrder <= :bigOrder) " +
                    "ORDER BY w.displayOrder ASC "
    )
    List<Work> readWorksToUpdateDisplayOrder(
            @Param("targetDate") LocalDate targetDate,
            @Param("user") User user,
            @Param("smallOrder") Integer smallOrder,
            @Param("bigOrder") Integer bigOrder
    );
}
