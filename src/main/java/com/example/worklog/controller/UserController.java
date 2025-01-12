package com.example.worklog.controller;


import com.example.worklog.dto.user.*;
import com.example.worklog.entity.RefreshTokenDetails;
import com.example.worklog.entity.User;
import com.example.worklog.dto.SuccessMessage;
import com.example.worklog.jwt.JwtDto;
import com.example.worklog.service.UserService;
import com.example.worklog.utils.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<SuccessMessage> register(@Valid @RequestBody UserSignupDto dto) {
        userService.register(
                dto.getEmail(),
                dto.getUsername(),
                dto.getPassword(),
                dto.getPasswordCheck()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessMessage.USER_CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(
            @Valid @RequestBody UserLoginDto dto,
            HttpServletRequest request
    ) {
        JwtDto jwtDto = userService.login(
                dto.getUsername(),
                dto.getPassword(),
                IpUtil.getClientIp(request)
        );
        return ResponseEntity.ok(jwtDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessMessage> logout(
            @AuthenticationPrincipal User user
    ) {
        userService.logout(user.getId());
        return ResponseEntity.ok(SuccessMessage.USER_LOGOUT_SUCCESS);
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtDto> reissue(
            Authentication authentication
    ) {
        JwtDto jwtDto = userService.reissue(
                (User) authentication.getPrincipal(),
                (RefreshTokenDetails) authentication.getCredentials()
        );
        return ResponseEntity.ok(jwtDto);
    }

    // 비밀번호 수정
    @PatchMapping("/me/password")
    public ResponseEntity<SuccessMessage> updatePassword(
            @Valid @RequestBody
            UserPasswordUpdateDto dto,
            @AuthenticationPrincipal User user
    ) {
        userService.updateUserPassword(
                dto.getCurrentPassword(),
                dto.getPassword(),
                dto.getPasswordCheck(),
                user.getId()
        );
        return ResponseEntity.ok(SuccessMessage.USER_PASSWORD_CHANGE_SUCCESS);
    }

    // 회원탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<SuccessMessage> deleteUser(@AuthenticationPrincipal User user) {
        userService.deleteUser(user.getId());
        return ResponseEntity.ok(SuccessMessage.USER_DELETE_SUCCESS);
    }

    // email 중복확인
    @GetMapping("/email/check")
    public ResponseEntity<SuccessMessage> checkEmail(@RequestParam String email) {
        userService.checkEmail(email);
        return ResponseEntity.ok(SuccessMessage.VALID_EMAIL);
    }

    // username 중복확인
    @GetMapping("/username/check")
    public ResponseEntity<SuccessMessage> checkUsername(@RequestParam String username) {
        userService.checkUsername(username);
        return ResponseEntity.ok(SuccessMessage.VALID_USERNAME);
    }

    @GetMapping("/password/check")
    public ResponseEntity<SuccessMessage> checkUsername(@RequestBody UserPasswordDto dto) {
        userService.checkPassword(dto.getPassword());
        return ResponseEntity.ok(SuccessMessage.VALID_PASSWORD);
    }

    @GetMapping("/password-check/check")
    public ResponseEntity<SuccessMessage> checkUsername(@RequestBody UserPasswordCheckDto dto) {
        userService.checkPasswordCheck(dto.getPassword(), dto.getPasswordCheck());
        return ResponseEntity.ok(SuccessMessage.VALID_PASSWORD_CHECK);
    }
}
