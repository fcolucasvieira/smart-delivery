package com.fcolucasvieira.smartdelivery.modules.orders.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateOrderResponse(UUID orderId, String status) {
}
