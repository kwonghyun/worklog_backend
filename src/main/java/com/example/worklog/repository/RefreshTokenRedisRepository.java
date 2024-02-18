package com.example.worklog.repository;

import com.example.worklog.entity.RefreshTokenDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenDetails, Long> {
    Optional<RefreshTokenDetails> findByRefreshToken(String refreshToken);
    boolean existsByUsername(String username);
    void deleteByUsername(String username);
}
