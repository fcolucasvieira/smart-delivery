package com.fcolucasvieira.smartdelivery.modules.products.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ListProductResponse(String name,
                                  String description,
                                  BigDecimal price) {
}
