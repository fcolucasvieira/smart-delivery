package com.fcolucasvieira.smartdelivery.modules.customers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CreateCustomerRequest(
        @NotBlank
        String name,

        @NotBlank
        @Pattern(regexp = "\\d{10,11}", message = "The phone number must contain 10 or 11 numeric digits")
        String phone,

        @NotBlank
        String email,

        String zipCode,

        String password) {
}
