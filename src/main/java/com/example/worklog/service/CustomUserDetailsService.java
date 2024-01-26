package com.example.worklog.service;


import com.example.worklog.dto.user.CustomUserDetails;
import com.example.worklog.entity.User;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
// TODO UserDetails 안쓰고 바로 User 객체 사용하게 이 클래스 없애기
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return CustomUserDetails.fromEntity(user);
    }
}
