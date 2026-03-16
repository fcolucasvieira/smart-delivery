package com.fcolucasvieira.smartdelivery.modules.deliveryman;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryManRepository extends JpaRepository<DeliveryManEntity, UUID> {
    Optional<DeliveryManEntity> findByDocument(String document);
}
