package com.example.worklog.utils;

import com.example.worklog.entity.Authority;
import com.example.worklog.entity.User;
import com.example.worklog.entity.enums.AuthorityType;
import com.example.worklog.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Initializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 테스트용 유저 생성
    @PostConstruct
    public void makeTestUser() {
        if (!userRepository.existsByUsername("1")) {
            User user1 = User.builder()
                    .email("a@a.a")
                    .password(passwordEncoder.encode("1"))
                    .username("1")
                    .lastNoticedAt(LocalDateTime.now().minusYears(1L))
                    .build();
            user1.addAuthority(
                    Authority.builder()
                            .user(user1)
                            .authorityType(AuthorityType.USER)
                            .build()
            );
            userRepository.save(user1);
        }
        if (!userRepository.existsByUsername("2")) {
            User user2 = User.builder()
                    .email("b@b.b")
                    .password(passwordEncoder.encode("2"))
                    .username("2")
                    .lastNoticedAt(LocalDateTime.now().minusYears(1L))
                    .build();
            user2.addAuthority(
                    Authority.builder()
                            .user(user2)
                            .authorityType(AuthorityType.USER)
                            .build()
            );
            userRepository.save(user2);
        }
        if (!userRepository.existsByUsername("3")) {
            User user3 = User.builder()
                    .email("c@c.c")
                    .password(passwordEncoder.encode("3"))
                    .username("3")
                    .lastNoticedAt(LocalDateTime.now().minusYears(1L))
                    .build();
            user3.addAuthority(
                    Authority.builder()
                            .user(user3)
                            .authorityType(AuthorityType.USER)
                            .build()
            );
            userRepository.save(user3);
        }
    }
}
