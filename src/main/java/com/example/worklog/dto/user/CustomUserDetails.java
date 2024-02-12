package com.example.worklog.dto.user;


import com.example.worklog.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private LocalDateTime lastNoticedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 현재 유저당 부여되는 권한은 하나임으로 하나만 추가함
        // 여러 Role 부여시 User의 Role 필드 수정 필요
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    // 사용자 식별 정보는 로그인 email
    @Override
    public String getUsername() {
        return this.username;
    }

    public Long getId() {
        return this.id;
    }

    public LocalDateTime getLastNoticedAt() {
        return this.lastNoticedAt;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static CustomUserDetails fromEntity(User user) {
        return CustomUserDetails.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .lastNoticedAt(user.getLastNoticedAt())
                .build();
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .password(password)
                .email(username)
                .lastNoticedAt(lastNoticedAt)
                .build();
    }
}
