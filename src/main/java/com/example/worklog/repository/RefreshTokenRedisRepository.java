package com.example.worklog.repository;

import com.example.worklog.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    boolean existsById(String username);
    void deleteById(String username);
}
