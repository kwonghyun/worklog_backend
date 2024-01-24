package com.example.worklog.controller;


import com.example.worklog.dto.ResourceResponseDto;
import com.example.worklog.dto.ResponseDto;
import com.example.worklog.dto.user.UserLoginDto;
import com.example.worklog.dto.user.UserSignupDto;
import com.example.worklog.dto.user.UserUpdatePwDto;
import com.example.worklog.exception.SuccessCode;
import com.example.worklog.jwt.JwtDto;
import com.example.worklog.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDto.fromSuccessCode(SuccessCode.USER_CREATED));
    }

    @PostMapping("/login")
    public ResponseEntity<ResourceResponseDto> login(
            @RequestBody UserLoginDto dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        JwtDto jwtDto = userService.login(dto, request);
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", jwtDto.getAccessToken())
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .build();


        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", jwtDto.getRefreshToken())
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResourceResponseDto.fromData(jwtDto, 2));
    }
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(
            HttpServletRequest request

    ) {
        userService.logout(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.LOGOUT_SUCCESS));
    }
    @PostMapping("/reissue")
    public ResponseEntity<ResourceResponseDto> reissue(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        JwtDto jwtDto = userService.reissue(request);
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", jwtDto.getAccessToken())
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", jwtDto.getRefreshToken())
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResourceResponseDto.fromData(jwtDto, 2));
    }

    // 비밀번호 수정
    @PatchMapping("/me/password")
    public ResponseEntity<ResponseDto> updatePassword(
            @Valid @RequestBody
            UserUpdatePwDto dto,
            Authentication auth
    ) {
        userService.updateUserPassword(dto, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.USER_PASSWORD_CHANGE_SUCCESS));
    }

    // 회원탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<ResponseDto> deleteUser(Authentication auth) {
        userService.deleteUser(auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.USER_DELETE_SUCCESS));
    }

    // email 중복확인
    @GetMapping("/email/check")
    public ResponseEntity<ResourceResponseDto> checkEmailDuplicated(@RequestParam String email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResourceResponseDto.fromData(userService.checkEmailDuplicated(email), 1));
    }

    // username 중복확인
    @GetMapping("/username/check")
    public ResponseEntity<ResourceResponseDto> checkUsernameDuplicated(@RequestParam String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResourceResponseDto.fromData(userService.checkUsernameDuplicated(username), 1));
    }
}
