package com.example.worklog.dto.user;

import com.example.worklog.utils.ValidationConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;


@Getter
public class UserSignupDto {

    @NotBlank(message = ValidationConstant.EMAIL_NOT_BLANK_MESSAGE)
    @Email(regexp = ValidationConstant.EMAIL_REGEX, message = ValidationConstant.EMAIL_NOT_VALID_MESSAGE)
    private String email;

    @NotBlank(message = ValidationConstant.USERNAME_NOT_BLANK_MESSAGE)
    @Pattern(regexp = ValidationConstant.USERNAME_REGEX, message = ValidationConstant.USERNAME_NOT_VALID_MESSAGE)
    private String username;

    @NotBlank(message = ValidationConstant.PASSWORD_NOT_BLANK_MESSAGE)
    @Pattern(regexp= ValidationConstant.PASSWORD_REGEX, message = ValidationConstant.PASSWORD_NOT_VALID_MESSAGE)
    private String password;

    @NotBlank(message = ValidationConstant.PASSWORD_CHECK_NOT_BLANK_MESSAGE)
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
