package com.payflow.auth.dto.response;

import com.payflow.auth.entity.Role;

import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        Role role,
        boolean enabled
) {}
