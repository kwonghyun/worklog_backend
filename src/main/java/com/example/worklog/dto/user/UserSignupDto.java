package com.example.worklog.dto.user;

import com.example.worklog.utils.Constant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;


@Getter
public class UserSignupDto {

    @NotBlank(message = Constant.EMAIL_NOT_BLANK_MESSAGE)
    @Email(regexp = Constant.EMAIL_REGEX, message = Constant.EMAIL_NOT_VALID_MESSAGE)
    private String email;

    @NotBlank(message = Constant.USERNAME_NOT_BLANK_MESSAGE)
    @Pattern(regexp = Constant.USERNAME_REGEX, message = Constant.USERNAME_NOT_VALID_MESSAGE)
    private String username;

    @NotBlank(message = Constant.PASSWORD_NOT_BLANK_MESSAGE)
    @Pattern(regexp= Constant.PASSWORD_REGEX, message = Constant.PASSWORD_NOT_VALID_MESSAGE)
    private String password;

    @NotBlank(message = Constant.PASSWORD_CHECK_NOT_BLANK_MESSAGE)
    private String passwordCheck;

    @Override
    public String toString() {
        return "UserSignupDto{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='***'" +
                ", passwordCheck='***'" +
                '}';
    }
}
