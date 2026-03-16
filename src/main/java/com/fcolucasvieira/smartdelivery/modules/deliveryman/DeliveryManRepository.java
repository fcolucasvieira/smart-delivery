package com.fcolucasvieira.smartdelivery.modules.deliveryman;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryManRepository extends JpaRepository<DeliveryManEntity, UUID> {
    Optional<DeliveryManEntity> findByDocument(String document);

    List<DeliveryManEntity> findByIsAvailable(boolean isAvailable);
}
