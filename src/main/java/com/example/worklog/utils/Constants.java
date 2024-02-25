package com.example.worklog.utils;

import java.time.format.DateTimeFormatter;

public class Constants {
    // 정규식
    public static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_.-]{5,20}$";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_.=+~-])(?=\\S+$).{8,20}$";
    public static final String DATE_TIME_REGEX = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";


    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME_SEC_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final DateTimeFormatter DATE_TIME_SEC_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_SEC_PATTERN);


    // 유효성 검사
    // 유저
    public static final String EMAIL_NOT_BLANK_MESSAGE = "이메일을 입력하세요.";
    public static final String EMAIL_NOT_VALID_MESSAGE = "올바른 이메일 형식으로 입력하세요.";
    public static final String USERNAME_NOT_BLANK_MESSAGE = "아이디를 입력하세요.";
    public static final String USERNAME_NOT_VALID_MESSAGE = "아이디는 영문과 숫자로 5~20자를 입력하세요.(허용되는 특수문자 '_', '-', '.')";
    public static final String PASSWORD_NOT_BLANK_MESSAGE = "비밀번호를 입력하세요.";
    public static final String PASSWORD_NOT_VALID_MESSAGE = "비밀번호는 8~20자의 영문 대·소문자, 숫자, 특수문자(!@#$%^&*()_-=+~)를 포함하여 입력하세요.";
    public static final String PASSWORD_CHECK_NOT_BLANK_MESSAGE = "비밀번호를 비밀번호 확인란에 다시 한번 입력하세요.";
    public static final String PASSWORD_CHECK_NOT_VALID_MESSAGE = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";


    // 날짜
    public static final String DATE_NOT_VALID_MESSAGE = "날짜를 yyyy-MM-dd 형식으로 입력하세요.";
    public static final String DATE_TIME_NOT_VALID_MESSAGE = "날짜를 yyyy-MM-dd HH:mm 형식으로 입력하세요.";
    public static final String YEAR_NOT_VALID_MESSAGE = "년도를 yyyy 형식으로 입력하세요.";
    public static final String MONTH_NOT_VALID_MESSAGE = "월을 1~12 사이의 값을 입력하세요.";

    // Enum
    public static final String CATEGORY_NOT_VALID_MESSAGE = "업무 유형을 올바르게 입력하세요.";
    public static final String WORK_STATE_NOT_VALID_MESSAGE = "업무 상태를 올바르게 입력하세요.";

    // work, memo
    public static final String TITLE_NOT_BLANK = "제목을 입력하세요.";
    public static final String CONTENT_NOT_BLANK = "내용을 입력하세요.";
    public static final String CATEGORY_NOT_BLANK = "올바른 업무 유형을 입력하세요.";
    public static final String WORK_STATE_NOT_BLANK = "올바른 업무 상태를 입력하세요.";
    public static final String DISPLAY_ORDER_NOT_VALID = "올바른 업무의 순번을 입력하세요.";

}
