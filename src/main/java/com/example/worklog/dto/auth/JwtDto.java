package com.example.worklog.dto.auth;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Builder
@Data
public class JwtDto {
    private String token;
}
