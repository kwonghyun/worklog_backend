package com.example.worklog.controller;


import com.example.worklog.dto.GetResponseDto;
import com.example.worklog.dto.ResponseDto;
import com.example.worklog.dto.user.UserLoginDto;
import com.example.worklog.dto.user.UserSignupDto;
import com.example.worklog.dto.user.UserUpdatePwDto;
import com.example.worklog.exception.SuccessCode;
import com.example.worklog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDto.fromSuccessCode(SuccessCode.USER_CREATED));
    }

    @PostMapping("/login")
    public ResponseEntity<GetResponseDto> login(
            @RequestBody UserLoginDto dto,
            HttpServletRequest request /*,
             HttpServletResponse response */
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GetResponseDto.getData(userService.login(dto, request /*, response*/)));
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
    public ResponseEntity<GetResponseDto> reissue(
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GetResponseDto.getData(userService.reissue(request)));
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
    public ResponseEntity<GetResponseDto> checkEmailDuplicated(@RequestParam String email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GetResponseDto.getData(userService.checkEmailDuplicated(email)));
    }

    // username 중복확인
    @GetMapping("/username/check")
    public ResponseEntity<GetResponseDto> checkUsernameDuplicated(@RequestParam String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GetResponseDto.getData(userService.checkUsernameDuplicated(username)));
    }
}
