package com.sails.client_connect.service;


import com.sails.client_connect.entity.RefreshToken;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.repository.RefreshTokenRepository;
import com.sails.client_connect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${refresh.token.expiry-time-ms}")
    private Long refreshTokenExpiryTimeMs;

    /**
     * @param username Checks if the username sent in request is in database
     *                 If satisfies then check if refresh token already present in database
     *                 If present then update it with new access token
     *                 Else Generate new refresh token
     * @return Refresh token
     */
    public RefreshToken createRefreshToken(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        RefreshToken refreshToken;

        if (existingToken.isPresent()) {
            refreshToken = existingToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiryTimeMs));
        } else {
            refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(refreshTokenExpiryTimeMs))
                    .build();
        }

        return refreshTokenRepository.save(refreshToken);
    }


    /**
     * @param token Find the token if it is in database
     * @return Refresh token
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * @param token Verify if the refresh token has expired
     * @return Refresh Token
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(" Refresh token was expired. Please make a new login request  " + token.getToken());
        }

        return token;
    }

}
