package com.fcolucasvieira.smartdelivery.modules.customers.repository;

import com.fcolucasvieira.smartdelivery.modules.customers.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    Optional<CustomerEntity> findByUserId(UUID userId);

    Optional<CustomerEntity> findByPhone(String phone);
}
