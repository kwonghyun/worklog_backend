package com.example.worklog.dto.user;

import com.example.worklog.utils.Constant;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserLoginDto {
    @NotBlank(message = Constant.USERNAME_NOT_BLANK_MESSAGE)
    private String username;

    @NotBlank(message = Constant.PASSWORD_NOT_BLANK_MESSAGE)
    private String password;

    @Override
    public String toString() {
        return "UserLoginDto{" +
                "username='" + username + '\'' +
                ", password='***'}";
    }
}
