package com.cavdar.employeemanagement.domain.repository;

import com.cavdar.employeemanagement.domain.model.RefreshToken;
import com.cavdar.employeemanagement.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    long deleteByUser(User user);
}