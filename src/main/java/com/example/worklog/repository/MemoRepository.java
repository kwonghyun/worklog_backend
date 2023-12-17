package com.example.worklog.repository;

import com.example.worklog.dto.work.RepoRequestParamDto;
import com.example.worklog.entity.Memo;
import com.example.worklog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    @Query(
            "SELECT m FROM Memo m " +
                "WHERE " +
                    "(:#{#dto.startDate} IS NULL OR m.date >= :#{#dto.startDate}) " +
                    "AND (:#{#dto.endDate} IS NULL OR m.date <= :#{#dto.endDate}) " +
                    "AND m.user = :user " +
            "ORDER BY m.id DESC " //+
//            "CASE WHEN :#{#dto.sortBy} = 'ID' THEN m.id END :#{#dto.direction}, " +
//            "CASE WHEN :#{#dto.sortBy} = 'MODIFIED_AT' THEN m.modifiedAt END :#{#dto.direction}, " +
//            "CASE WHEN :#{#dto.sortBy} = 'ORDER' THEN m.order END :#{#dto.direction}"
    )
    Page<Memo> readMemosByParamsAndUser(@Param("dto") RepoRequestParamDto dto, @Param("user") User user, Pageable pageable);
}
