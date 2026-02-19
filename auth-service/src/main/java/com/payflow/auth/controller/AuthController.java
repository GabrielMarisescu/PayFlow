package com.payflow.auth.controller;

import com.payflow.auth.dto.request.LoginRequest;
import com.payflow.auth.dto.request.RefreshRequest;
import com.payflow.auth.dto.request.RegisterRequest;
import com.payflow.auth.dto.response.AuthResponse;
import com.payflow.auth.dto.response.UserProfileResponse;
import com.payflow.auth.security.JwtService;
import com.payflow.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(
            @RequestHeader("Authorization") String authHeader,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String token = authHeader.substring(7);
        UUID userId = UUID.fromString(jwtService.extractUserId(token));
        authService.logoutAll(userId);
        return ResponseEntity.noContent().build();
    }
}
