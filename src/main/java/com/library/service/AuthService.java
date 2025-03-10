package com.library.service;

import com.library.dto.AuthRequest;
import com.library.dto.AuthResponse;
import com.library.dto.RegisterRequest;
import com.library.model.Role;
import com.library.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        log.debug("Registering new user: {}", request.getUsername());
        
        if (userService.existsByUsername(request.getUsername())) {
            log.warn("Username already exists: {}", request.getUsername());
            throw new RuntimeException("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        log.debug("Password encoded for new user");

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(encodedPassword)
                .role(Role.READER)
                .createdAt(LocalDateTime.now())
                .build();

        userService.createUser(user);
        log.debug("User registered successfully: {}", user.getUsername());

        String token = jwtService.generateToken(userService.loadUserByUsername(user.getUsername()));
        log.debug("JWT token generated for new user");

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        log.debug("Attempting to authenticate user: {}", request.getUsername());
        
        try {
            // Проверяем существование пользователя
            User user = userService.getUserByUsername(request.getUsername());
            log.debug("User found in database: {}", user.getUsername());
            
            // Пытаемся аутентифицировать
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            
            log.debug("Authentication successful for user: {}", authentication.getName());

            String token = jwtService.generateToken(userService.loadUserByUsername(user.getUsername()));
            log.debug("JWT token generated successfully");

            return AuthResponse.builder()
                    .token(token)
                    .username(user.getUsername())
                    .role(user.getRole())
                    .build();
            
        } catch (Exception e) {
            log.error("Authentication failed for user: {}. Error: {}", request.getUsername(), e.getMessage());
            throw new RuntimeException("Invalid username or password");
        }
    }
} 