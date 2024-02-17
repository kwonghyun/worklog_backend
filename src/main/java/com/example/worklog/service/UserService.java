package com.example.worklog.service;

import com.example.worklog.dto.user.*;
import com.example.worklog.entity.Authority;
import com.example.worklog.entity.NotificationFlag;
import com.example.worklog.entity.RefreshToken;
import com.example.worklog.entity.User;
import com.example.worklog.entity.enums.AuthorityType;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.jwt.JwtDto;
import com.example.worklog.jwt.JwtTokenUtils;
import com.example.worklog.repository.NotificationFlagRedisRepository;
import com.example.worklog.repository.RefreshTokenRedisRepository;
import com.example.worklog.repository.UserRepository;
import com.example.worklog.utils.ValidationConstant;
import com.example.worklog.utils.EnvironmentVariable;
import com.example.worklog.utils.IpUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
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
    private final NotificationFlagRedisRepository notificationFlagRedisRepository;
    private final PasswordEncoder passwordEncoder;
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
    public JwtDto login(UserLoginDto dto, HttpServletRequest request) {
        // TODO 같은 ID로 5회이상 로그인 실패시 계정 잠그고 이메일 인증으로 비밀번호 재설정하게 하기
        Pattern usernamePattern = Pattern.compile(ValidationConstant.USERNAME_REGEX);
        Pattern passwordPattern = Pattern.compile(ValidationConstant.PASSWORD_REGEX);
        if (
                !usernamePattern.matcher(dto.getUsername()).matches()
                || !passwordPattern.matcher(dto.getPassword()).matches()
        ) {
            if (dto.getUsername().length() != 1 || dto.getPassword().length() != 1) {
                throw new CustomException(ErrorCode.LOGIN_FAILED);
            }
        }

        User user = userRepository.findByUsernameWithAuthority(dto.getUsername())
                    .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        log.info("\"{}\" 로그인", dto.getUsername());

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            log.info("login: 비밀번호 불일치");
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }
        log.info("login: 비밀번호 확인완료");
        LocalDateTime lastNoticedAt = user.getLastNoticedAt();
        if (isTimeToNotice(user.getLastNoticedAt())) {
            user.updateLastNoticedAt(LocalDateTime.now());
            notificationFlagRedisRepository.save(
                    NotificationFlag.builder()
                            .userId(user.getId())
                            .build()
            );
        }
        JwtDto jwtDto = jwtTokenUtils.generateToken(
                user.getId(), user.getUsername(), user.getLastNoticedAt(), user.getAuthorities()
        );

        // 유효기간 초단위 설정 후 redis에 timeToLive 설정
        Claims refreshTokenClaims = jwtTokenUtils.parseClaims(jwtDto.getRefreshToken());
        Long validPeriod
                = refreshTokenClaims.getExpiration().toInstant().getEpochSecond()
                    - refreshTokenClaims.getIssuedAt().toInstant().getEpochSecond();
        log.info("뭐가문제야");
        refreshTokenRedisRepository.save(
                RefreshToken.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .lastNoticedAt(user.getLastNoticedAt())
                        .authorities(jwtTokenUtils.getStringFromAuthorities(user.getAuthorities()))
                        .ip(IpUtil.getClientIp(request))
                        .ttl(validPeriod)
                        .refreshToken(jwtDto.getRefreshToken())
                        .build()
        );
        log.info("뭐가문제야2");
        return jwtDto;
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

    public JwtDto reissue(HttpServletRequest request) {
        // 1. 레디스에 해당 토큰 있는 지 확인
        RefreshToken refreshToken = refreshTokenRedisRepository
                .findByRefreshToken(request.getHeader("Authorization").split(" ")[1])
                .orElseThrow(() -> new CustomException(ErrorCode.WRONG_REFRESH_TOKEN));

        // 2. 리프레시 토큰을 발급한 IP와 동일한 IP에서 온 요청인지 확인
        if (!IpUtil.getClientIp(request).equals(refreshToken.getIp())) {
            throw new CustomException(ErrorCode.IP_NOT_MATCHED);
        }

        // 3. 리프레시 토큰에서 username 찾기
        LocalDateTime lastNoticedAt = refreshToken.getLastNoticedAt();
        JwtDto jwtDto;
        if (isTimeToNotice(lastNoticedAt)) {
            User user = userRepository.findByIdWithAuthority(refreshTokenDetails.getUserId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            user.updateLastNoticedAt(LocalDateTime.now());
            notificationFlagRedisRepository.save(
                    NotificationFlag.builder()
                            .userId(user.getId())
                            .build()
            );
            jwtDto = jwtTokenUtils.generateToken(
                    user.getId(), user.getUsername(), user.getLastNoticedAt(), user.getAuthorities()
            );
        } else {

            jwtDto = jwtTokenUtils.generateToken(
                    refreshToken.getUserId(), refreshToken.getUsername(), refreshToken.getLastNoticedAt(), jwtTokenUtils.getAuthoritiesFromString(refreshToken.getAuthorities())
            );
        }

        log.info("reissue: refresh token 재발급 완료");

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

    public void checkEmail(String email) {
        Pattern emailPattern = Pattern.compile(ValidationConstant.EMAIL_REGEX);
        if (!emailPattern.matcher(email).matches()) {
            throw new CustomException(ErrorCode.WRONG_EMAIL_FORMAT);
        }
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.ALREADY_EXISTED_EMAIL);
        }
    }

    public void checkUsername(String username) {
        Pattern usernamePattern = Pattern.compile(ValidationConstant.USERNAME_REGEX);
        if (!usernamePattern.matcher(username).matches()) {
            throw new CustomException(ErrorCode.WRONG_USERNAME_FORMAT);
        }
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.ALREADY_EXISTED_USERNAME);
        }
    }

    public void checkPassword(String password) {
        Pattern passwordPattern = Pattern.compile(ValidationConstant.PASSWORD_REGEX);
        if (!passwordPattern.matcher(password).matches()) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD_FORMAT);
        }
    }

    public void checkPasswordCheck(UserPasswordCheckDto dto) {
        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new CustomException(ErrorCode.UNMATCHED_PASSWORD);
        }
    }

    public void deleteUser(String username) {
        log.info("{} 회원 탈퇴 시작", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (refreshTokenRedisRepository.existsById(username)) {
            refreshTokenRedisRepository.deleteById(username);
            log.info("레디스에서 리프레시 토큰 삭제 완료");
        }
        userRepository.delete(user);
        log.info("{} 회원 탈퇴 완료", username);
    }

    public void updateUserPassword(UserPasswordUpdateDto dto, String username) {
        Pattern passwordPattern = Pattern.compile(ValidationConstant.PASSWORD_REGEX);
        if (!passwordPattern.matcher(dto.getCurrentPassword()).matches()) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

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
    public Boolean isTimeToNotice(LocalDateTime lastNoticedAt) {
        return lastNoticedAt == null
                || lastNoticedAt
                .plusHours(EnvironmentVariable.SEARCH_FUTURE_NOTIFICATION_MINUTES)
                .minusSeconds(jwtTokenUtils.getAccessExpirationTime())
                .isAfter(LocalDateTime.now());
    }

    private String getStringFromAuthorities(Collection<Authority> authorities) {
        String authoritiesString = authorities.stream()
                .map(authority -> authority.getAuthorityType().name()).collect(Collectors.joining(","));
        return authoritiesString;
    }
}