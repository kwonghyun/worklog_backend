package com.example.worklog.dto.user;

import lombok.Getter;

@Getter
public class UserPasswordCheckReq {
    private String password;
    private String passwordCheck;
}
