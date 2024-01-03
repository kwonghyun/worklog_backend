package com.example.worklog.service;

import com.example.worklog.jwt.JwtDto;
import com.example.worklog.dto.user.UserLoginDto;
import com.example.worklog.dto.user.UserSignupDto;
import com.example.worklog.dto.user.UserUpdatePwDto;
import com.example.worklog.entity.User;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.jwt.JwtTokenUtils;
import com.example.worklog.jwt.RefreshToken;
import com.example.worklog.repository.RefreshTokenRedisRepository;
import com.example.worklog.repository.UserRepository;
import com.example.worklog.utils.IpUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
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

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

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
        log.info("회원가입: username 중복 확인 {}", dto.getUsername());
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new CustomException(ErrorCode.ALREADY_EXISTED_USERNAME);
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .build();
        userRepository.save(user);

        log.info("회원가입 완료 pk:{}, email:{}, nickname:{}",
                user.getId(), user.getEmail(), user.getUsername());
    }

    // 로그인
    public JwtDto login(UserLoginDto dto, HttpServletRequest request /*, HttpServletResponse response */) {
        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());
        log.info("\"{}\" 로그인", dto.getUsername());

        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            log.info("login: 비밀번호 불일치");
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
        log.info("login: 비밀번호 확인완료");

        JwtDto jwtDto = jwtTokenUtils.generateToken(userDetails);

        // 유효기간 초단위 설정 후 redis에 timeToLive 설정
        Claims refreshTokenClaims = jwtTokenUtils.parseClaims(jwtDto.getRefreshToken());
        Long validPeriod
                = refreshTokenClaims.getExpiration().toInstant().getEpochSecond()
                - refreshTokenClaims.getIssuedAt().toInstant().getEpochSecond();
        refreshTokenRedisRepository.save(
                RefreshToken.builder()
                        .id(dto.getUsername())
                        .ip(IpUtil.getClientIp(request))
                        .ttl(validPeriod)
                        .refreshToken(jwtDto.getRefreshToken())
                        .build()
        );
        return jwtDto;


        // 응답 헤더에 jwt 전달
//        response.setHeader("Authorization", "Bearer " + jwtToken);
//        log.info("Header: {}", response.getHeader("Authorization"));
//        return JwtDto.builder().accessToken(jwtToken).build();
    }
    public void logout(HttpServletRequest request) {
        // 1. 레디스에 해당 토큰 있는 지 확인
        String accessToken = request.getHeader("Authorization").split(" ")[1];

        // 2. 리프레시 토큰을 username으로 찾아 삭제
        String username = jwtTokenUtils.parseClaims(accessToken).getSubject();
        log.info("access token에서 추출한 username : {}", username);
        if (refreshTokenRedisRepository.existsById(username)) {
            refreshTokenRedisRepository.deleteById(username);
            log.info("레디스에서 리프레시 토큰 삭제 완료");
        } else {
            new CustomException(ErrorCode.WRONG_REFRESH_TOKEN);
        }
    }

    public JwtDto reissue(HttpServletRequest request /*, HttpServletResponse response */) {
        // 1. 레디스에 해당 토큰 있는 지 확인
        RefreshToken refreshToken = refreshTokenRedisRepository
                .findByRefreshToken(request.getHeader("Authorization").split(" ")[1])
                .orElseThrow(() -> new CustomException(ErrorCode.WRONG_REFRESH_TOKEN));

        // 2. 리프레시 토큰을 발급한 IP와 동일한 IP에서 온 요청인지 확인
        if (!IpUtil.getClientIp(request).equals(refreshToken.getIp())) {
            throw new CustomException(ErrorCode.IP_NOT_MATCHED);
        }

        // 3. 리프레시 토큰에서 username 찾기
        String username = refreshToken.getId();
        log.info("refresh token에서 추출한 username : {}", username);
        // 4. userdetails 불러오기
        UserDetails userDetails = manager.loadUserByUsername(username);

        log.info("reissue: refresh token 재발급 완료");
        JwtDto jwtDto = jwtTokenUtils.generateToken(userDetails);
        refreshToken.updateRefreshToken(jwtDto.getRefreshToken());
        // 유효기간 초단위 설정 후 redis에 timeToLive 설정
        Claims refreshTokenClaims = jwtTokenUtils.parseClaims(jwtDto.getRefreshToken());
        Long validPeriod
                = refreshTokenClaims.getExpiration().toInstant().getEpochSecond()
                - refreshTokenClaims.getIssuedAt().toInstant().getEpochSecond();
        refreshToken.updateTtl(validPeriod);
        refreshTokenRedisRepository.save(refreshToken);
        return jwtDto;
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
        if (refreshTokenRedisRepository.existsById(username)) {
            refreshTokenRedisRepository.deleteById(username);
            log.info("레디스에서 리프레시 토큰 삭제 완료");
        }
        userRepository.delete(user);
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