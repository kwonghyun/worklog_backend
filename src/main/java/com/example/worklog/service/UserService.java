package com.example.worklog.service;

import com.example.worklog.dto.auth.JwtDto;
import com.example.worklog.dto.user.UserLoginDto;
import com.example.worklog.dto.user.UserSignupDto;
import com.example.worklog.dto.user.UserUpdatePwDto;
import com.example.worklog.entity.User;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.jwt.JwtTokenUtils;
import com.example.worklog.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsManager manager;
    private final JwtTokenUtils jwtTokenUtils;


    public void register(UserSignupDto dto) {
        log.info("회원가입: 비밀번호 입력 확인");
        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new CustomException(ErrorCode.UNMATCHED_PASSWORD);
        }
        log.info("회원가입: email 중복 확인 {}", dto.getEmail());
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_EXISTED_EMAIL);
        }
        log.info("회원가입: nickname 중복 확인 {}", dto.getUsername());
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new CustomException(ErrorCode.ALREADY_EXISTED_USERNAME);
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .isDeleted(false)
                .build();
        userRepository.save(user);

        log.info("회원가입 완료 pk:{}, email:{}, nickname:{}",
                user.getId(), user.getEmail(), user.getUsername());
    }

    // 로그인
    public JwtDto login(UserLoginDto dto, HttpServletResponse response) {
        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());
        log.info("\"{}\" 로그인", dto.getUsername());

        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            log.info("login: 비밀번호 불일치");
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        log.info("login: 비밀번호 확인완료");
        String jwtToken = jwtTokenUtils.generateToken(userDetails);

        // 응답 헤더에 jwt 전달
        response.setHeader("Authorization", "Bearer " + jwtToken);
        log.info("Header: {}", response.getHeader("Authorization"));
        return JwtDto.builder().token(jwtToken).build();
    }

    public Boolean checkEmailDuplicated(String email) {
        log.info("{} 중복검사 existsByEmail: {}", email, userRepository.existsByEmail(email));
        return userRepository.existsByEmail(email);
    }

    public Boolean checkUsernameDuplicated(String username) {
        log.info("{} 중복검사 existsByUsername: {}", username, userRepository.existsByUsername(username));
        return userRepository.existsByUsername(username);
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (!user.getIsDeleted()) {
            user.delete();
        }
        userRepository.save(user);
        log.info("{} 회원 탈퇴 완료", username);
    }

    public void updateUserPassword(UserUpdatePwDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        log.info("비밀번호 수정");
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            log.info("비밀번호 불일치");
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            log.info("비밀번호 확인과 입력 틀림");
            throw new CustomException(ErrorCode.UNMATCHED_PASSWORD);
        }
        if (passwordEncoder.matches(dto.getCurrentPassword(), dto.getPassword())) {
            log.info("기존과 동일한 비밀번호");
            throw new CustomException(ErrorCode.ALREADY_USED_PASSWORD);
        }
        user.updatePassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        log.info("비밀번호 수정 완료");
    }
}
