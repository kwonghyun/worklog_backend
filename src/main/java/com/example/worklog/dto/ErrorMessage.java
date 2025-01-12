package com.example.worklog.dto;

import com.example.worklog.utils.Constants;
import lombok.Getter;

@Getter
public enum ErrorMessage {

    // 400 BAD_REQUEST 잘못된 요청
    ALREADY_EXISTED_USERNAME("이미 존재하는 아이디입니다"),
    ALREADY_EXISTED_EMAIL("이미 존재하는 이메일입니다"),
    ALREADY_USED_PASSWORD("기존에 사용하던 비밀번호입니다."),

    WRONG_EMAIL_FORMAT(Constants.EMAIL_NOT_VALID_MESSAGE),
    WRONG_USERNAME_FORMAT(Constants.USERNAME_NOT_VALID_MESSAGE),
    WRONG_PASSWORD_FORMAT(Constants.PASSWORD_NOT_VALID_MESSAGE),
    UNMATCHED_PASSWORD(Constants.PASSWORD_CHECK_NOT_VALID_MESSAGE),

    // 404 NOT_FOUND 잘못된 리소스 접근,
    USER_NOT_FOUND("존재하지 않는 회원입니다."),
    LOGIN_FAILED("아이디와 비밀번호를 다시 확인해주세요."),
    WRONG_PASSWORD("비밀번호가 틀렸습니다."),

    // Memo
    MEMO_NOT_FOUND("존재하지 않는 메모입니다."),
    MEMO_USER_NOT_MATCHED("접근권한이 없는 메모입니다."),
    MEMO_ORDER_INVALID(Constants.DISPLAY_ORDER_NOT_VALID),

    // Work
    WORK_NOT_FOUND("존재하지 않는 업무입니다."),
    WORK_USER_NOT_MATCHED("접근권한이 없는 업무입니다."),
    WORK_ORDER_INVALID(Constants.DISPLAY_ORDER_NOT_VALID),

    // Notification
    NOTIFICATION_NOT_FOUND("존재하지 않는 알림입니다."),
    SSE_CONNECTION_BROKEN("SSE 연결이 끊겼습니다."),
    SSE_CONNECTION_NOT_FOUND("SSE 연결이 없습니다."),

    // Scheduler
    SCHEDULER_FAILED("스케줄러에 문제가 생겼습니다."),

    // JWT 관련 에러
    TOKEN_NO_AUTH("권한 정보가 없는 토큰입니다."),
    IP_NOT_MATCHED("리프레시 토큰의 IP주소가 일치하지 않습니다."),
    TOKEN_INVALID("유효하지 않은 토큰입니다."),
    WRONG_SIGNATURE_TOKEN("유효하지 않은 토큰입니다."),
    NOT_SUPPORTED_TOKEN("유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED("토큰 유효기간이 만료되었습니다."),
    WRONG_REFRESH_TOKEN("잘못된 리프레시 토큰입니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
