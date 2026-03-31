package com.fcolucasvieira.smartdelivery.modules.products.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateProductResponse(UUID id,
                                    String name) {
}
