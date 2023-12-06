package com.example.worklog.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    ERROR_BAD_REQUEST(400, "BAD_REQUEST", "Bad request"),
    ERROR_UNAUTHORIZED(401, "UNAUTHORIZED", "Unauthorized access"),
    ERROR_FORBIDDEN(403, "FORBIDDEN", "Access forbidden"),
    ERROR_NOT_FOUND(404, "NOT_FOUND", "Resource not found"),
    ERROR_METHOD_NOT_ALLOWED(405, "METHOD_NOT_ALLOWED", "Method not allowed"),
    ERROR_CONFLICT(409, "CONFLICT", "Conflict with existing resource"),
    ERROR_LENGTH_REQUIRED(411, "LENGTH_REQUIRED", "Length required"),
    ERROR_PRECONDITION_FAILED(412, "PRECONDITION_FAILED", "Precondition failed"),
    ERROR_UNSUPPORTED_MEDIA(415, "UNSUPPORTED_MEDIA", "Unsupported media type"),
    ERROR_TOO_MANY_REQUESTS(429, "TOO_MANY_REQUESTS", "Too many requests"),
    ERROR_INTERNAL_SERVER(500, "INTERNAL_SERVER", "Internal server error"),
    ERROR_SERVICE_UNAVAILABLE(503, "SERVICE_UNAVAILABLE", "Service temporarily unavailable"),
    ERROR_GATEWAY_TIMEOUT(504, "GATEWAY_TIMEOUT", "Gateway timeout"),
    ERROR_NETWORK_AUTHENTICATION_REQUIRED(511, "NETWORK_AUTH_REQUIRED", "Network authentication required"),

    // 400 BAD_REQUEST 잘못된 요청
    ALREADY_EXISTED_USERNAME(400, "BAD_REQUEST", "이미 존재하는 아이디입니다"),
    ALREADY_EXISTED_EMAIL(400, "BAD_REQUEST", "이미 존재하는 이메일입니다"),
    ALREADY_USED_PASSWORD(400, "BAD_REQUEST", "기존에 사용하던 비밀번호입니다."),

    INVALID_PARAMETER(400, "BAD_REQUEST","파라미터 값을 확인해주세요."),
    ERROR_NO_KEYWORD(400,"BAD_REQUEST","검색어를 입력해주세요."),

    // 403 FORBIDDEN 권한이 없는 경우
    USER_NO_AUTH(403, "FORBIDDEN", "해당 기능에 대해 권한이 없습니다."),


    // 404 NOT_FOUND 잘못된 리소스 접근
    WRONG_PASSWORD(404, "NOT_FOUND", "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(404, "NOT_FOUND", "존재하지 않는 회원입니다."),
    UNMATCHED_PASSWORD(404, "NOT_FOUND", "비밀번호 확인이 일치하지 않습니다."),


    // 500 내부 서버 에러
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR","No message available."),

    // JWT 관련 에러
    TOKEN_NO_AUTH(403, "FORBIDDEN", "권한 정보가 없는 토큰입니다."),
    IP_NOT_MATCHED(403, "FORBIDDEN", "리프레시 토큰의 IP주소가 일치하지 않습니다."),
    TOKEN_INVALID(403, "FORBIDDEN", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(403, "FORBIDDEN", "토큰 유효기간이 만료되었습니다."),
    WRONG_REFRESH_TOKEN(404, "NOT_FOUND", "일치하는 리프레시 토큰이 없습니다.");

    private final int status;
    private final String code;
    private final String message;
    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
