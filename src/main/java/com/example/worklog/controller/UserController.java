package com.example.worklog.controller;


import com.example.worklog.dto.ResponseDto;
import com.example.worklog.dto.user.UserLoginDto;
import com.example.worklog.dto.user.UserSignupDto;
import com.example.worklog.dto.user.UserUpdatePwDto;
import com.example.worklog.exception.SuccessCode;
import com.example.worklog.exception.SuccessDto;
import com.example.worklog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<SuccessDto> register(@Valid @RequestBody UserSignupDto dto) {
        userService.register(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessDto.fromSuccessCode(SuccessCode.USER_CREATED));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(
            @RequestBody UserLoginDto dto,
            HttpServletRequest request /*,
             HttpServletResponse response */
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.getData(userService.login(dto, request /*, response*/)));
    }
    @PostMapping("/reissue")
    public ResponseEntity<ResponseDto> reissue(
            HttpServletRequest request /*,
             HttpServletResponse response */
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.getData(userService.reissue(request /*, response*/)));
    }

    // 비밀번호 수정
    @PatchMapping("/me/password")
    public ResponseEntity<SuccessDto> updatePassword(@Valid @RequestBody UserUpdatePwDto dto, Authentication auth) {
        userService.updateUserPassword(dto, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessDto.fromSuccessCode(SuccessCode.USER_PASSWORD_CHANGE_SUCCESS));
    }

    // 회원탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<SuccessDto> deleteUser(Authentication auth) {
        userService.deleteUser(auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessDto.fromSuccessCode(SuccessCode.USER_DELETE_SUCCESS));
    }

    // email 중복확인
    @GetMapping("/email/check")
    public ResponseEntity<ResponseDto> checkEmailDuplicated(@RequestParam String email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.getData(userService.checkEmailDuplicated(email)));
    }

    // username 중복확인
    @GetMapping("/username/check")
    public ResponseEntity<ResponseDto> checkUsernameDuplicated(@RequestParam String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.getData(userService.checkUsernameDuplicated(username)));
    }
}
