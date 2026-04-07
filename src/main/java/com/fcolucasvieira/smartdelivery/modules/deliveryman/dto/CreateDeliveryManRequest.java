package com.fcolucasvieira.smartdelivery.modules.deliveryman.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.br.CPF;

@Builder
public record CreateDeliveryManRequest (
        @NotBlank
        String name,

        @NotBlank
        @CPF
        String document,

        @NotBlank
        @Pattern(regexp = "\\d{10,11}", message = "The phone number must contain 10 or 11 numeric digits")
        String phone
) { }
