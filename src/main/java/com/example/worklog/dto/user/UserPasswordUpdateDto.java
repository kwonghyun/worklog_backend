package com.example.worklog.dto.user;


import com.example.worklog.utils.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserPasswordUpdateDto {
    @NotBlank(message = Constants.PASSWORD_NOT_BLANK_MESSAGE)
    private String currentPassword;

    @NotBlank(message = Constants.PASSWORD_NOT_BLANK_MESSAGE)
    @Pattern(regexp= Constants.PASSWORD_REGEX, message = Constants.PASSWORD_NOT_VALID_MESSAGE)
    private String password;

    @NotBlank(message = Constants.PASSWORD_CHECK_NOT_BLANK_MESSAGE)
    private String passwordCheck;
}
