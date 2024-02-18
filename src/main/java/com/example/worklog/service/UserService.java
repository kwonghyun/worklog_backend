package com.example.worklog.service;

import com.example.worklog.entity.Authority;
import com.example.worklog.entity.RefreshTokenDetails;
import com.example.worklog.entity.User;
import com.example.worklog.entity.enums.AuthorityType;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.jwt.JwtDto;
import com.example.worklog.jwt.JwtTokenUtils;
import com.example.worklog.repository.RefreshTokenRedisRepository;
import com.example.worklog.repository.UserRepository;
import com.example.worklog.utils.Constants;
import com.example.worklog.utils.EnvironmentVariable;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    public void register(String email, String username, String password, String passwordCheck) {
        // TODO 이메일 인증 가입
        checkEmail(email);
        checkUsername(username);
        checkPassword(password);
        checkPasswordCheck(password, passwordCheck);

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .username(username)
                .lastNoticedAt(LocalDateTime.now().minusDays(1L))
                .build();
        user.addAuthority(Authority.builder()
                .user(user)
                .authorityType(AuthorityType.USER)
                .build());
        userRepository.save(user);

        log.info("회원가입 완료 pk:{}, email:{}, nickname:{}",
                user.getId(), user.getEmail(), user.getUsername());
    }

    // 로그인
    public JwtDto login(String username, String password, String clientIp) {
        // TODO 같은 ID로 5회이상 로그인 실패시 계정 잠그고 이메일 인증으로 비밀번호 재설정하게 하기
        Pattern usernamePattern = Pattern.compile(Constants.USERNAME_REGEX);
        Pattern passwordPattern = Pattern.compile(Constants.PASSWORD_REGEX);
        if (
                !usernamePattern.matcher(username).matches()
                || !passwordPattern.matcher(password).matches()
        ) {
            if (username.length() != 1 || password.length() != 1) {
                log.info("username: {} ");
                throw new CustomException(ErrorCode.LOGIN_FAILED);
            }
        }

        User user = userRepository.findByUsernameWithAuthority(username)
                    .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));
        log.info("로그인 시도: {}", user.toString());

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.info("login: 비밀번호 불일치");
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }
        log.info("login: 비밀번호 확인완료");

        if (isTimeToNotice(user.getLastNoticedAt())) {
            user.updateLastNoticedAt(LocalDateTime.now());
            notificationService.produceNotificationFlag(user.getId());
            log.info("마지막 알림 보낸 시간 업데이트, NotificationFlag 생성");
        }

        JwtDto jwtDto = jwtTokenUtils.generateToken(user);

        // 유효기간 초단위 설정 후 redis에 timeToLive 설정
        Claims refreshTokenClaims = jwtTokenUtils.parseClaims(jwtDto.getRefreshToken());
        refreshTokenRedisRepository.save(
                RefreshTokenDetails.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .lastNoticedAt(user.getLastNoticedAt())
                        .authorities(user.getStringFromAuthorities())
                        .ip(clientIp)
                        .ttl(getTokenValidSeconds(refreshTokenClaims))
                        .refreshToken(jwtDto.getRefreshToken())
                        .build()
        );
        log.info("사용자 권한: {}", user.getStringFromAuthorities());
        return jwtDto;
    }

    public void logout(Long userId) {
        refreshTokenRedisRepository.deleteById(userId);
    }

    public JwtDto reissue(User user, RefreshTokenDetails refreshTokenDetails) {
        if (isTimeToNotice(user.getLastNoticedAt())) {
            user.updateLastNoticedAt(LocalDateTime.now());
            notificationService.produceNotificationFlag(user.getId());
        }
        JwtDto jwtDto = jwtTokenUtils.generateToken(user);
        refreshTokenDetails.updateRefreshToken(jwtDto.getRefreshToken());

        // 유효기간 초단위 설정 후 redis에 timeToLive 설정
        Claims refreshTokenClaims = jwtTokenUtils.parseClaims(jwtDto.getRefreshToken());
        refreshTokenDetails.updateTtl(
                getTokenValidSeconds(refreshTokenClaims)
        );
        refreshTokenRedisRepository.save(refreshTokenDetails);
        log.info("refreshTokenDetails: {}", refreshTokenDetails);
        return jwtDto;
    }

    private Long getTokenValidSeconds(Claims tokenClaims) {
        return tokenClaims.getExpiration().toInstant().getEpochSecond()
                - tokenClaims.getIssuedAt().toInstant().getEpochSecond();
    }

    public void checkEmail(String email) {
        Pattern emailPattern = Pattern.compile(Constants.EMAIL_REGEX);
        if (!emailPattern.matcher(email).matches()) {
            throw new CustomException(ErrorCode.WRONG_EMAIL_FORMAT);
        }
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.ALREADY_EXISTED_EMAIL);
        }
    }

    public void checkUsername(String username) {
        Pattern usernamePattern = Pattern.compile(Constants.USERNAME_REGEX);
        if (!usernamePattern.matcher(username).matches()) {
            throw new CustomException(ErrorCode.WRONG_USERNAME_FORMAT);
        }
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.ALREADY_EXISTED_USERNAME);
        }
    }

    public void checkPassword(String password) {
        Pattern passwordPattern = Pattern.compile(Constants.PASSWORD_REGEX);
        if (!passwordPattern.matcher(password).matches()) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD_FORMAT);
        }
    }

    public void checkPasswordCheck(String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new CustomException(ErrorCode.UNMATCHED_PASSWORD);
        }
    }

    public void deleteUser(Long userId) {
        logout(userId);
        userRepository.deleteById(userId);
    }

    public void updateUserPassword(String currentPassword, String newPassword, String newPasswordCheck, Long userId) {
        Pattern passwordPattern = Pattern.compile(Constants.PASSWORD_REGEX);
        if (!passwordPattern.matcher(currentPassword).matches()) {
            log.info("비밀번호 패턴 유효하지 않음.");
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        if (!newPassword.equals(newPasswordCheck)) {
            log.info("비밀번호 확인과 입력 틀림");
            throw new CustomException(ErrorCode.UNMATCHED_PASSWORD);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            log.info("비밀번호 불일치");
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
        if (currentPassword.equals(newPassword)) {
            log.info("기존과 동일한 비밀번호");
            throw new CustomException(ErrorCode.ALREADY_USED_PASSWORD);
        }
        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("비밀번호 수정 완료");
    }
    private Boolean isTimeToNotice(LocalDateTime lastNoticedAt) {
        return lastNoticedAt
                .plusMinutes(EnvironmentVariable.SEARCH_FUTURE_NOTIFICATION_MINUTES)
                .minusSeconds(jwtTokenUtils.getAccessExpirationTime())
                .isBefore(LocalDateTime.now());
    }

}