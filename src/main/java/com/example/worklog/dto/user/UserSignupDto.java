package com.example.worklog.dto.user;

import com.example.worklog.utils.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;


@Getter
public class UserSignupDto {

    @NotBlank(message = Constants.EMAIL_NOT_BLANK_MESSAGE)
    @Email(regexp = Constants.EMAIL_REGEX, message = Constants.EMAIL_NOT_VALID_MESSAGE)
    private String email;

    @NotBlank(message = Constants.USERNAME_NOT_BLANK_MESSAGE)
    @Pattern(regexp = Constants.USERNAME_REGEX, message = Constants.USERNAME_NOT_VALID_MESSAGE)
    private String username;

    @NotBlank(message = Constants.PASSWORD_NOT_BLANK_MESSAGE)
    @Pattern(regexp= Constants.PASSWORD_REGEX, message = Constants.PASSWORD_NOT_VALID_MESSAGE)
    private String password;

    @NotBlank(message = Constants.PASSWORD_CHECK_NOT_BLANK_MESSAGE)
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
