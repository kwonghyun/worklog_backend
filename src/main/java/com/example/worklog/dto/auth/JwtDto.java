package com.example.worklog.dto.auth;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtDto {
    private String accessToken;
    private String refreshToken;
}
