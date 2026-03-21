package com.fcolucasvieira.smartdelivery.modules.products.dto;

import java.util.UUID;

public record CreateProductResponse(UUID id,
                                    String name) {
}
