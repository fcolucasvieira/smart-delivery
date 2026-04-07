package com.fcolucasvieira.smartdelivery.modules.deliveryman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "delivery_men")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryManEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Modelar na próxima migration
    @Column(nullable = false)
    private String name;

    // Modelar na próxima migration
    @Column(nullable = false, unique = true)
    private String document;

    // Modelar na próxima migration
    @Column(nullable = false, unique = true)
    private String phone;

    private boolean isAvailable;
}
