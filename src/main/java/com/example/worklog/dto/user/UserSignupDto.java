package com.example.worklog.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
public class UserSignupDto {
    @Email(message = "이메일 형식으로 입력해주세요") @NotBlank
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&])(?=\\S+$).{8,20}",
            message = "비밀번호는 8~20자 사이로 영문 대,소문자와 숫자, 특수기호(!@#$%^&)를 포함하여 작성해주세요.")
    private String password;
    @NotBlank(message = "비밀번호를 다시 한번 입력해주세요.")
    private String passwordCheck;
    @NotBlank
    @Size(max = 12, message = "닉네임은 한글자 이상 열두글자 이하로 작성해주세요.")
    private String username;
}
