package com.payflow.auth.controller;

import com.payflow.auth.dto.request.RoleUpdateRequest;
import com.payflow.auth.dto.request.UpdateProfileRequest;
import com.payflow.auth.dto.response.UserProfileResponse;
import com.payflow.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getCurrentUser(userDetails.getUsername()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMe(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(userService.updateProfile(userDetails.getUsername(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<UserProfileResponse> assignRole(
            @PathVariable UUID id,
            @Valid @RequestBody RoleUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.assignRole(id, request));
    }

    @DeleteMapping("/{id}/roles")
    public ResponseEntity<UserProfileResponse> revokeRole(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.revokeRole(id));
    }
}
