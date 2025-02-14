package com.example.worklog.repository;

import com.example.worklog.entity.Memo;
import com.example.worklog.repository.querydsl.CalendarRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Memo, Long>, CalendarRepositoryCustom {
//    @Query(
//            nativeQuery = true,
//            value =
//                    "SELECT combinded_dates.year FROM " +
//                            "( " +
//                            "(SELECT DISTINCT EXTRACT(YEAR FROM m.date)  AS year FROM memo m WHERE (m.is_deleted = false) AND (m.user_id = :userId)) " +
//                            "UNION (SELECT DISTINCT EXTRACT(YEAR FROM w.date) AS year FROM \"work\" w WHERE (w.is_deleted = false) AND (w.user_id = :userId)) " +
//                            ") combinded_dates " +
//                            "ORDER BY year ASC "
//    )
//    List<Integer> readDistinctYear(@Param("userId") Long userId);
//
//    @Query(
//            nativeQuery = true,
//            value =
//                    "SELECT combinded_dates.month FROM " +
//                            "( " +
//                            "(SELECT DISTINCT EXTRACT(MONTH FROM m.date) AS month FROM memo m WHERE (EXTRACT(YEAR FROM m.date) = :year) AND (m.is_deleted = false) AND (m.user_id = :userId)) " +
//                            "UNION (SELECT DISTINCT EXTRACT(MONTH FROM w.date) AS month FROM \"work\" w WHERE (EXTRACT(YEAR FROM w.date) = :year) AND (w.is_deleted = false) AND (w.user_id = :userId)) " +
//                            ") combinded_dates " +
//                            "ORDER BY month ASC "
//    )
//    List<Integer> readDistinctMonthsByYear(
//            @Param("year") int year,
//            @Param("userId") Long userId
//    );
//
//    @Query(
//            nativeQuery = true,
//            value =
//                    "SELECT combinded_dates.day FROM " +
//                            "( " +
//                            "(SELECT DISTINCT EXTRACT(DAY FROM m.date) AS day FROM memo m WHERE (EXTRACT(YEAR FROM m.date) = :year) AND (EXTRACT(MONTH FROM m.date) = :month) AND (m.is_deleted = false) AND (m.user_id = :userId)) " +
//                            "UNION (SELECT DISTINCT EXTRACT(DAY FROM w.date) AS day FROM \"work\" w WHERE (EXTRACT(YEAR FROM w.date) = :year) AND (EXTRACT(MONTH FROM w.date) = :month) AND (w.is_deleted = false) AND (w.user_id = :userId)) " +
//                            ") combinded_dates " +
//                            "ORDER BY day ASC "
//    )
//    List<Integer> readDistinctDaysByYearAndMonth(
//            @Param("year") int year,
//            @Param("month") int month,
//            @Param("userId") Long userId
//    );

}
