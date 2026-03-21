package com.fcolucasvieira.smartdelivery.modules.orders.dto;

import java.util.UUID;

public record OrderItemRequest(
        UUID productId,
        Integer quantity
) {
}
