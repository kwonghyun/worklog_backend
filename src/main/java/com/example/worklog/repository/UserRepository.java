package com.example.worklog.repository;

import com.example.worklog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query(
            "SELECT u FROM User u JOIN FETCH u.authorities " +
                    "WHERE u.username =:username"
    )
    Optional<User> findByUsernameWithAuthority(@Param("username") String username);

    @Query(
            "SELECT u FROM User u JOIN FETCH u.authorities " +
                    "WHERE u.id =:userId"
    )
    Optional<User> findByIdWithAuthority(@Param("userId") Long userId);
}
