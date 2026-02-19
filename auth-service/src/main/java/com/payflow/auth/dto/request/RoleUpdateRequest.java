package com.payflow.auth.dto.request;

import com.payflow.auth.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleUpdateRequest {

    @NotNull
    private Role role;
}
