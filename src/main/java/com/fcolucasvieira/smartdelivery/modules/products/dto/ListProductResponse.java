package com.fcolucasvieira.smartdelivery.modules.products.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ListProductResponse(UUID id,
                                  String name,
                                  String description,
                                  BigDecimal price) {
}
