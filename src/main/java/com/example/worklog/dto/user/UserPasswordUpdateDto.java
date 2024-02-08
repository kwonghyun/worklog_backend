package com.example.worklog.dto.user;


import com.example.worklog.utils.Constant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserPasswordUpdateDto {
    @NotBlank(message = Constant.PASSWORD_NOT_BLANK_MESSAGE)
    private String currentPassword;

    @NotBlank(message = Constant.PASSWORD_NOT_BLANK_MESSAGE)
    @Pattern(regexp= Constant.PASSWORD_REGEX, message = Constant.PASSWORD_NOT_VALID_MESSAGE)
    private String password;

    @NotBlank(message = Constant.PASSWORD_CHECK_NOT_BLANK_MESSAGE)
    private String passwordCheck;
}
