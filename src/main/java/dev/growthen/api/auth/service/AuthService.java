package dev.growthen.api.auth.service;

import dev.growthen.api.auth.dto.request.LoginRequest;
import dev.growthen.api.auth.dto.request.RefreshTokenRequest;
import dev.growthen.api.auth.dto.request.RegisterRequest;
import dev.growthen.api.auth.dto.response.AuthResponse;
import dev.growthen.api.auth.entity.RefreshToken;
import dev.growthen.api.auth.repository.RefreshTokenRepository;
import dev.growthen.api.common.constants.ErrorMessages;
import dev.growthen.api.common.exception.DuplicateResourceException;
import dev.growthen.api.common.exception.ResourceNotFoundException;
import dev.growthen.api.common.exception.UnauthorizedException;
import dev.growthen.api.config.jwt.JwtService;
import dev.growthen.api.user.entity.User;
import dev.growthen.api.user.enums.Role;
import dev.growthen.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsernameAndIsDeletedFalse(request.getUsername())) {
            throw new DuplicateResourceException(ErrorMessages.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
            throw new DuplicateResourceException(ErrorMessages.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        
        return generateAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsernameAndIsDeletedFalse(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));

        // Revoke all previous tokens for security
        refreshTokenRepository.revokeAllUserTokens(user.getUsername());
        
        return generateAuthResponse(user);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String requestToken = request.getRefreshToken();
        
        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestToken)
                .orElseThrow(() -> new UnauthorizedException(ErrorMessages.INVALID_REFRESH_TOKEN));
                
        if (refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException(ErrorMessages.INVALID_REFRESH_TOKEN);
        }
        
        User user = refreshToken.getUser();
        
        // Re-validate against JWT signature and expiry
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
                
        if (!jwtService.isRefreshTokenValid(requestToken, userDetails)) {
            throw new UnauthorizedException(ErrorMessages.INVALID_REFRESH_TOKEN);
        }

        // Generate new access token
        String newAccessToken = jwtService.generateToken(user);
        
        return new AuthResponse(
                newAccessToken,
                requestToken,
                "Bearer",
                jwtService.getExpirationTime()
        );
    }

    public void logout(String username) {
        refreshTokenRepository.revokeAllUserTokens(username);
    }

    private AuthResponse generateAuthResponse(User user) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(LocalDateTime.now().plus(jwtService.getRefreshExpirationTime(), java.time.temporal.ChronoUnit.MILLIS))
                .revoked(false)
                .build();
                
        refreshTokenRepository.save(refreshTokenEntity);
        
        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtService.getExpirationTime()
        );
    }
}
