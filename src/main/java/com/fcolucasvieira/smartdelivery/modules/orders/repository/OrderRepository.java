package com.fcolucasvieira.smartdelivery.modules.orders.repository;

import com.fcolucasvieira.smartdelivery.modules.orders.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
