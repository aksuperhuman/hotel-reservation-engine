package com.enterprise.hotel.auth;

import com.enterprise.hotel.auth.dto.AuthResponse;
import com.enterprise.hotel.auth.dto.LoginRequest;
import com.enterprise.hotel.auth.dto.RefreshRequest;
import com.enterprise.hotel.auth.dto.RegisterRequest;
import com.enterprise.hotel.common.exception.BusinessException;
import com.enterprise.hotel.security.JwtService;
import com.enterprise.hotel.user.RefreshToken;
import com.enterprise.hotel.user.RefreshTokenRepository;
import com.enterprise.hotel.user.User;
import com.enterprise.hotel.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       @Value("${app.jwt.access-token-expiration-ms}") long accessExpirationMs,
                       @Value("${app.jwt.refresh-token-expiration-ms}") long refreshExpirationMs) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(HttpStatus.CONFLICT, "Email already registered");
        }
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .role(request.role())
                .enabled(true)
                .build();
        userRepository.save(user);
        return issueTokens(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        return issueTokens(user);
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest request) {
        RefreshToken stored = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));
        if (stored.isRevoked() || stored.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Refresh token expired or revoked");
        }
        // Rotate: revoke the used token and issue a fresh pair.
        stored.setRevoked(true);
        refreshTokenRepository.save(stored);
        return issueTokens(stored.getUser());
    }

    private AuthResponse issueTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole().name());
        String refreshValue = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(refreshValue)
                .expiresAt(Instant.now().plusMillis(refreshExpirationMs))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
        return new AuthResponse(accessToken, refreshValue, "Bearer", accessExpirationMs);
    }
}
