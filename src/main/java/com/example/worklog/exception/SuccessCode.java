package com.example.worklog.exception;

import lombok.Getter;

@Getter
public enum SuccessCode {

    // 기본 코드
    SUCCESS(200, "SUCCESS", "요청이 성공했습니다."),
    CREATED(201, "CREATED", "요청에 따른 자원이 생성되었습니다."),

    // 유저,
    USER_CREATED(201, "CREATED", "회원가입이 완료되었습니다."),
    USER_LOGOUT_SUCCESS(200, "OK", "로그아웃 되었습니다."),
    USER_PASSWORD_CHANGE_SUCCESS(200, "OK", "비밀번호가 변경되었습니다."),
    USER_DELETE_SUCCESS(200, "OK", "회원탈퇴가 완료되었습니다."),

    // work
    WORK_CREATED(201, "CREATED", "업무일지가 생성되었습니다."),
    WORK_EDIT_SUCCESS(200, "OK", "업무일지가 수정되었습니다."),
    WORK_DELETE_SUCCESS(200, "OK", "업무일지가 삭제되었습니다."),

    // savedWork
    SAVED_WORK_CREATED(201, "CREATED", "업무일지가 북마크되었습니다."),
    SAVED_WORK_CANCEL_SUCCESS(200, "OK", "업무일지가 북마크 해제되었습니다."),

    // memo
    MEMO_CREATED(201, "CREATED", "메모가 생성되었습니다."),
    MEMO_EDIT_SUCCESS(200, "OK", "메모가 수정되었습니다."),
    MEMO_DELETE_SUCCESS(200, "OK", "메모가 삭제되었습니다."),

    // savedWork
    SAVED_MEMO_CREATED(201, "CREATED", "메모가 북마크되었습니다."),
    SAVED_MEMO_CANCEL_SUCCESS(200, "OK", "메모가 북마크 해제되었습니다.");

    private final int status;
    private final String code;
    private final String message;

    SuccessCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
