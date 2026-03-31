package com.fcolucasvieira.smartdelivery.modules.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateProductRequest (
        @NotBlank
        String name,

        String description,

        @Positive
        BigDecimal price) {}
