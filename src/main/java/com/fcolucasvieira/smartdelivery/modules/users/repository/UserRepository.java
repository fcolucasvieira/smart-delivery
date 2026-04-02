package com.fcolucasvieira.smartdelivery.modules.users.repository;

import com.fcolucasvieira.smartdelivery.modules.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
}
