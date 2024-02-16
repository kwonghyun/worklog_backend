package com.example.worklog.dto.user;


import com.example.worklog.utils.ValidationConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserPasswordUpdateDto {
    @NotBlank(message = ValidationConstant.PASSWORD_NOT_BLANK_MESSAGE)
    private String currentPassword;

    @NotBlank(message = ValidationConstant.PASSWORD_NOT_BLANK_MESSAGE)
    @Pattern(regexp= ValidationConstant.PASSWORD_REGEX, message = ValidationConstant.PASSWORD_NOT_VALID_MESSAGE)
    private String password;

    @NotBlank(message = ValidationConstant.PASSWORD_CHECK_NOT_BLANK_MESSAGE)
    private String passwordCheck;
}
