package com.example.worklog.dto.user;

import com.example.worklog.utils.Constant;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    @NotBlank(message = Constant.USERNAME_NOT_BLANK_MESSAGE)
    private String username;

    @NotBlank(message = Constant.PASSWORD_NOT_BLANK_MESSAGE)
    private String password;
}
