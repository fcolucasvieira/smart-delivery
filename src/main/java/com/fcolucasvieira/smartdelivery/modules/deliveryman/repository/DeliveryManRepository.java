package com.fcolucasvieira.smartdelivery.modules.deliveryman.repository;

import com.fcolucasvieira.smartdelivery.modules.deliveryman.entity.DeliveryManEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryManRepository extends JpaRepository<DeliveryManEntity, UUID> {
    Optional<DeliveryManEntity> findByDocument(String document);

    Optional<DeliveryManEntity> findByPhone(String phone);

    List<DeliveryManEntity> findByIsAvailable(boolean isAvailable);
}
