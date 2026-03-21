package com.fcolucasvieira.smartdelivery.modules.orders.dto;

import com.fcolucasvieira.smartdelivery.modules.orders.OrderItemEntity;

import java.util.List;

public record CreateOrderRequest(List<OrderItemRequest> items) {}
