package com.fcolucasvieira.smartdelivery.modules.orders.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CompleteOrderResponse(
        UUID orderId,
        String status,
        UUID deliveryManId
) {
}
