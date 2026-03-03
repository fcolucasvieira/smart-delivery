package com.fcolucasvieira.smartdelivery.modules.orders.dto;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(UUID customerId, List<UUID> productsIds) {}
