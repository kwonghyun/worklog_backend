package com.example.worklog.utils;

import com.example.worklog.entity.User;
import com.example.worklog.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 테스트용 유저 생성
    @PostConstruct
    public void makeTestUser() {
        if (!userRepository.existsByEmail("a@a.a")) {
            User user1 = User.builder()
                    .email("a@a.a")
                    .password(passwordEncoder.encode("1"))
                    .username("1")
                    .build();
            userRepository.save(user1);
        }
        if (!userRepository.existsByEmail("b@b.b")) {
            User user2 = User.builder()
                    .email("b@b.b")
                    .password(passwordEncoder.encode("2"))
                    .username("2")
                    .build();
            userRepository.save(user2);
        }
        if (!userRepository.existsByEmail("c@c.c")) {
            User user3 = User.builder()
                    .email("c@c.c")
                    .password(passwordEncoder.encode("3"))
                    .username("3")
                    .build();
            userRepository.save(user3);
        }
    }
}
