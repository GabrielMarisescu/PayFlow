package com.payflow.auth.service;

import com.payflow.auth.dto.request.RoleUpdateRequest;
import com.payflow.auth.dto.request.UpdateProfileRequest;
import com.payflow.auth.dto.response.UserProfileResponse;
import com.payflow.auth.entity.Role;
import com.payflow.auth.entity.User;
import com.payflow.auth.exception.UserNotFoundException;
import com.payflow.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .map(this::toProfileResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserById(UUID id) {
        return userRepository.findById(id)
                .map(this::toProfileResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return toProfileResponse(userRepository.save(user));
    }

    public UserProfileResponse assignRole(UUID userId, RoleUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setRole(request.getRole());
        return toProfileResponse(userRepository.save(user));
    }

    public UserProfileResponse revokeRole(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setRole(Role.USER);
        return toProfileResponse(userRepository.save(user));
    }

    private UserProfileResponse toProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.isEnabled()
        );
    }
}
