package com.fcolucasvieira.smartdelivery.modules.orders.dto;

import java.util.List;

public record CreateOrderRequest(List<OrderItemRequest> items) {}
