package com.example.worklog.jwt;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtDto {
    private String accessToken;
    private String refreshToken;
}
