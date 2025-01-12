package com.example.worklog.dto;

import lombok.Getter;

@Getter
public enum SuccessMessage {

    // 기본 코드
    SUCCESS("성공했습니다."),

    // 유저,
    USER_CREATED("가입을 했습니다."),
    USER_PASSWORD_CHANGE_SUCCESS("비밀번호를 변경했습니다."),
    USER_DELETE_SUCCESS("탈퇴했습니다."),
    VALID_EMAIL("사용 가능한 이메일입니다."),
    VALID_USERNAME("사용 가능한 아이디입니다."),
    VALID_PASSWORD("사용 가능한 비밀번호입니다."),
    VALID_PASSWORD_CHECK("비밀번호가 일치합니다."),

    // work
    WORK_CREATED("저장했습니다."),
    WORK_EDIT_SUCCESS("수정했습니다."),
    WORK_DELETE_SUCCESS("삭제했습니다."),

    // memo
    MEMO_CREATED("저장했습니다."),
    MEMO_EDIT_SUCCESS("수정했습니다."),
    MEMO_DELETE_SUCCESS("삭제했습니다."),

    // token
    USER_LOGOUT_SUCCESS("로그아웃했습니다.");

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
