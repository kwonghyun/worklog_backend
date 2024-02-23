package com.example.worklog.service;

import com.example.worklog.entity.RefreshTokenDetails;
import com.example.worklog.entity.User;
import com.example.worklog.jwt.JwtDto;

public interface UserService {
    public void register(String email, String username, String password, String passwordCheck);
    public JwtDto login(String username, String password, String clientIp);
    public void logout(Long userId);
    public JwtDto reissue(User user, RefreshTokenDetails refreshTokenDetails);
    public void checkEmail(String email);
    public void checkUsername(String username);
    public void checkPassword(String password);
    public void checkPasswordCheck(String password, String passwordCheck);
    public void deleteUser(Long userId);
    public void updateUserPassword(String currentPassword, String newPassword, String newPasswordCheck, Long userId);
}