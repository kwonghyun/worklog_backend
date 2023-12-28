package com.example.worklog.repository;

import com.example.worklog.entity.Memo;
import com.example.worklog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Memo, Long> {
    @Query(
            nativeQuery = true,
            value =
                    "SELECT combinded_dates.year FROM " +
                            "( " +
                                "(SELECT DISTINCT YEAR(m.date)  AS year FROM Memo m WHERE (m.is_deleted = false) AND (m.user_id = :userId)) " +
                                "UNION (SELECT DISTINCT YEAR(w.date) AS year FROM Work w WHERE (w.is_deleted = false) AND (w.user_id = :userId)) " +
                            ") combinded_dates " +
                            "ORDER BY year ASC "
    )
    List<Integer> readDistinctYear(@Param("userId") Long userId);

    @Query(
            nativeQuery = true,
            value =
                    "SELECT combinded_dates.month FROM " +
                            "( " +
                                "(SELECT DISTINCT MONTH(m.date) AS month FROM Memo m WHERE (YEAR(m.date) = :year) AND (m.is_deleted = false) AND (m.user_id = :userId)) " +
                                "UNION (SELECT DISTINCT MONTH(w.date) AS month FROM Work w WHERE (YEAR(w.date) = :year) AND (w.is_deleted = false) AND (w.user_id = :userId)) " +
                            ") combinded_dates " +
                            "ORDER BY month ASC "
    )
    List<Integer> readDistinctMonthsByYear(
            @Param("year") int year,
            @Param("userId") Long userId
    );

    @Query(
            nativeQuery = true,
            value =
                    "SELECT combinded_dates.day FROM " +
                            "( " +
                                "(SELECT DISTINCT DAY(m.date) AS day FROM Memo m WHERE (YEAR(m.date) = :year) AND (MONTH(m.date) = :month) AND (m.is_deleted = false) AND (m.user_id = :userId)) " +
                                "UNION (SELECT DISTINCT DAY(w.date) AS day FROM Work w WHERE (YEAR(w.date) = :year) AND (MONTH(w.date) = :month) AND (w.is_deleted = false) AND (w.user_id = :userId)) " +
                            ") " +
                            "combinded_dates " +
                            "ORDER BY day ASC "
    )
    List<Integer> readDistinctDaysByYearAndMonth(
            @Param("year") int year,
            @Param("month") int month,
            @Param("userId") Long userId
    );
}
