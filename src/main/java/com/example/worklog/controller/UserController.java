package com.example.worklog.controller;

import com.example.worklog.dto.ResponseDto;
import com.example.worklog.dto.auth.JwtDto;
import com.example.worklog.dto.user.UserLoginDto;
import com.example.worklog.dto.user.UserSignupDto;
import com.example.worklog.dto.user.UserUpdatePwDto;
import com.example.worklog.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResponseDto> register(@Valid @RequestBody UserSignupDto dto) {
        userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.getMessage("회원가입이 완료되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody UserLoginDto dto, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(dto, response));
    }

    // 비밀번호 수정
    @PatchMapping("/me/password")
    public ResponseEntity<ResponseDto> updatePassword(@Valid @RequestBody UserUpdatePwDto dto, Authentication auth) {
        userService.updateUserPassword(dto, auth.getName());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto().getMessage("비밀번호가 수정되었습니다."));
    }

    // 회원탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<ResponseDto> deleteUser(Authentication auth) {
        userService.deleteUser(auth.getName());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto().getMessage("회원탈퇴가 완료되었습니다."));
    }

    // email 중복확인
    // POST /users/check-email/
    @GetMapping("/check/email")
    public Boolean checkEmailDuplicated(@RequestParam String email) {
        return userService.checkEmailDuplicated(email);
    }

    // username 중복확인
    @GetMapping("/check/username")
    public Boolean checkUsernameDuplicated(@RequestParam String username) {
        return userService.checkUsernameDuplicated(username);
    }
}
