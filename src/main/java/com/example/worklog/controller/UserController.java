package com.example.worklog.controller;


import com.example.worklog.dto.ResourceResponseDto;
import com.example.worklog.dto.ResponseDto;
import com.example.worklog.dto.user.*;
import com.example.worklog.entity.User;
import com.example.worklog.exception.SuccessCode;
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
    public ResponseEntity<ResponseDto> register(@Valid @RequestBody UserSignupDto dto) {
        userService.register(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDto.fromSuccessCode(SuccessCode.USER_CREATED));
    }

    @PostMapping("/login")
    public ResponseEntity<ResourceResponseDto> login(
            @RequestBody UserLoginDto dto,
            HttpServletRequest request
    ) {

        JwtDto jwtDto = userService.login(
                dto.getUsername(), dto.getPassword(), IpUtil.getClientIp(request)
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResourceResponseDto.fromData(jwtDto, 2));
    }
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(
            Authentication authentication
    ) {
        userService.logout((String) authentication.getCredentials());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.LOGOUT_SUCCESS));
    }
    @PostMapping("/reissue")
    public ResponseEntity<ResourceResponseDto> reissue(
            HttpServletRequest request
    ) {
        String refreshToken = request.getHeader("Authorization").split(" ")[1];
        JwtDto jwtDto = userService.reissue(refreshToken, IpUtil.getClientIp(request));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResourceResponseDto.fromData(jwtDto, 2));
    }

    // 비밀번호 수정
    @PatchMapping("/me/password")
    public ResponseEntity<ResponseDto> updatePassword(
            @Valid @RequestBody
            UserPasswordUpdateDto dto,
            @AuthenticationPrincipal User user
    ) {
        userService.updateUserPassword(
                dto.getCurrentPassword(),
                dto.getPassword(),
                dto.getPasswordCheck(),
                user.getUsername()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.USER_PASSWORD_CHANGE_SUCCESS));
    }

    // 회원탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<ResponseDto> deleteUser(@AuthenticationPrincipal User user) {
        userService.deleteUser(user.getUsername());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.USER_DELETE_SUCCESS));
    }

    // email 중복확인
    @GetMapping("/email/check")
    public ResponseEntity<ResponseDto> checkEmail(@RequestParam String email) {
        userService.checkEmail(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.VALID_EMAIL));
    }

    // username 중복확인
    @GetMapping("/username/check")
    public ResponseEntity<ResponseDto> checkUsername(@RequestParam String username) {
        userService.checkUsername(username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.VALID_USERNAME));
    }

    @GetMapping("/password/check")
    public ResponseEntity<ResponseDto> checkUsername(@RequestBody UserPasswordDto dto) {
        userService.checkPassword(dto.getPassword());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.VALID_PASSWORD));
    }

    @GetMapping("/password-check/check")
    public ResponseEntity<ResponseDto> checkUsername(@RequestBody UserPasswordCheckDto dto) {
        userService.checkPasswordCheck(dto.getPassword(), dto.getPasswordCheck());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.VALID_PASSWORD_CHECK));
    }
}
