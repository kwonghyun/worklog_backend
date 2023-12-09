package com.example.worklog.dto.user;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdatePwDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&])(?=\\S+$).{8,15}",
            message = "비밀번호는 8~15자 사이로 영문 대,소문자와 숫자, 특수기호(!@#$%^&)를 포함하여 작성해주세요.")
    private String password;

    @NotBlank(message = "비밀번호를 비밀번호 확인란에 다시 한번 입력해주세요.")
    private String passwordCheck;
}
