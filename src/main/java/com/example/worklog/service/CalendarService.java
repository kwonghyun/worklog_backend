package com.example.worklog.service;

import com.example.worklog.dto.calendar.*;
import com.example.worklog.entity.User;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.MemoRepository;
import com.example.worklog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarService {
    private final MemoRepository memoRepository;
    private final UserRepository userRepository;

    public YearResponseDto readYears(String username) {
        User user = getValidatedUserByUsername(username);

        List<Integer> yearsFromMemo = memoRepository.readDistinctYear(user);
        return YearResponseDto.fromList(yearsFromMemo);
    }

    public MonthResponseDto readMonths(MonthRequestDto dto, String username) {
        User user = getValidatedUserByUsername(username);

        List<Integer> monthsFromMemo = memoRepository.readDistinctMonthsByYear(dto.getYear(), user);
        return MonthResponseDto.fromList(monthsFromMemo);
    }

    public DayResponseDto readDays(DayRequestDto dto, String username) {
        User user = getValidatedUserByUsername(username);

        List<Integer> daysFromMemo = memoRepository.readDistinctDaysByYearAndMonth(dto.getYear(), dto.getMonth(), user);
        return DayResponseDto.fromList(daysFromMemo);
    }

    private User getValidatedUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
