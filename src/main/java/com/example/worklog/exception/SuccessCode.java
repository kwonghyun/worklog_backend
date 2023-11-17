package com.example.worklog.exception;

import lombok.Getter;

@Getter
public enum SuccessCode {
    SUCCESS(200, "Success"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted");

    private final int status;
    private final String message;

    SuccessCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
