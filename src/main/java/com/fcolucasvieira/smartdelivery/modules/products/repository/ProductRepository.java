package com.fcolucasvieira.smartdelivery.modules.products.repository;

import com.fcolucasvieira.smartdelivery.modules.products.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}
