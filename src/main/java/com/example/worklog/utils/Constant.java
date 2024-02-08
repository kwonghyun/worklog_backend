package com.example.worklog.utils;

public class Constant {
    public static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9]{5,20}$";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_-=+~])(?=\\S+$).{8,20}$";

    public static final String EMAIL_NOT_BLANK_MESSAGE = "이메일을 입력하세요.";
    public static final String USERNAME_NOT_BLANK_MESSAGE = "아이디를 입력하세요.";
    public static final String PASSWORD_NOT_BLANK_MESSAGE = "비밀번호를 입력하세요.";
    public static final String PASSWORD_CHECK_NOT_BLANK_MESSAGE = "비밀번호를 비밀번호 확인란에 다시 한번 입력하세요.";

    public static final String EMAIL_NOT_VALID_MESSAGE = "올바른 이메일 형식으로 입력하세요.";
    public static final String USERNAME_NOT_VALID_MESSAGE = "아이디는 영문과 숫자로 5~20자를 입력하세요.";
    public static final String PASSWORD_NOT_VALID_MESSAGE = "비밀번호는 8~20자의 영문 대,소문자, 숫자, 특수문자(!@#$%^&*()_-=+~)를 포함하여 입력해주세요.";
    public static final String PASSWORD_CHECK_NOT_VALID_MESSAGE = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
    public static final long WORK_DEADLINE_TRIGGER_HOURS = 24L;
    public static final long SEARCH_FUTURE_NOTIFICATION_MINUTES = 60L;

}
