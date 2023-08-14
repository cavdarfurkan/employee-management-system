package com.cavdar.employeemanagement.domain.service;

import com.cavdar.employeemanagement.domain.model.RefreshToken;
import com.cavdar.employeemanagement.domain.model.User;
import com.cavdar.employeemanagement.domain.repository.RefreshTokenRepository;
import com.cavdar.employeemanagement.util.exception.RefreshTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshTokenDurationMs;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    public RefreshToken findByToken(String token) {
        return this.refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException(token, "Refresh token is not in database!"));
    }

    public RefreshToken createRefreshToken(Long userId) {
        User user = this.userService.getUserById(userId);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return this.refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            this.refreshTokenRepository.delete(token);
            throw new RefreshTokenException(token.getToken(), "Refresh token was expired. Please make a new login request.");
        }

        return token;
    }

    @Transactional
    public long deleteByUserId(Long userId) {
        return this.refreshTokenRepository.deleteByUser(
                this.userService.getUserById(userId)
        );
    }
}
