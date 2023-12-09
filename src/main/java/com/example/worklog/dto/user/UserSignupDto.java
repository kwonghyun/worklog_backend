package com.example.worklog.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
public class UserSignupDto {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&])(?=\\S+$).{8,20}",
            message = "비밀번호는 8~20자 사이로 영문과 숫자, 특수기호(!@#$%^&)를 포함하여 작성해주세요.")
    private String password;

    @NotBlank(message = "비밀번호를 비밀번호 확인란에 다시 한번 입력해주세요.")
    private String passwordCheck;

    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(max = 20, message = "로그인할 아이디를 20자 이하로 작성해주세요.")
    private String username;
}
