package com.example.worklog.dto.user;

import lombok.Data;

@Data
public class UserPasswordCheckDto {
    private String password;
    private String passwordCheck;
}
