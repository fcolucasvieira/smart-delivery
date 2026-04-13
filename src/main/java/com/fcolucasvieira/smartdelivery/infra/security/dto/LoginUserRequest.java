package com.fcolucasvieira.smartdelivery.infra.security.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest(
        @NotBlank
        String username,

        @NotBlank
        String password
) {
}
