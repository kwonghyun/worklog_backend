package com.example.worklog.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class StringConverter {

    public static String convertSnakeToCamel(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (currentChar == '-' || currentChar == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(currentChar));
                    capitalizeNext = false;
                } else {
                    result.append(currentChar);
                }
            }
        }
        return result.toString();
    }
    private static LocalDateTime extractDeadlineFromMessage(String message) {
        String dateTimeRegex = Constants.DATE_TIME_REGEX;
        Pattern pattern = Pattern.compile(dateTimeRegex);
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String matchedText = matcher.group();
            return LocalDateTime.parse(matchedText, Constants.DATE_TIME_FORMAT);
        } else {
            log.info("잘못된 알림 메세지: {}", message);
            return null;
        }
    }

    private static String getRestWorkNotificationMessage(LocalDateTime deadline) {
        LocalDateTime now = LocalDateTime.now();
        long minDiff = Math.abs(ChronoUnit.MINUTES.between(deadline, now));
        long hour = minDiff / 60;
        long minute = minDiff % 60;
        String timeDiff = hour == 0 ?
                String.format("%d분", minute) : String.format("%d시간 %d분", hour, minute);
        String isExpired = deadline.isBefore(now) ? "지났" : "남았";
        return String.format("%s %s습니다.", timeDiff, isExpired);
    }

    public static String completeWorkNotificationMessage(String message) {
        LocalDateTime deadline = extractDeadlineFromMessage(message);
        String restMessage = getRestWorkNotificationMessage(deadline);
        return message + restMessage;
    }
}
