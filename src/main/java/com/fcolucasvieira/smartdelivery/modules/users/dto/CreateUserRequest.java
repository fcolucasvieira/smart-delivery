package com.fcolucasvieira.smartdelivery.modules.users.dto;

import com.fcolucasvieira.smartdelivery.modules.users.entity.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateUserRequest(
        @NotBlank
        String username,

        @NotBlank
        String password,

        @NotNull
        UserRole userRole
) {
}
