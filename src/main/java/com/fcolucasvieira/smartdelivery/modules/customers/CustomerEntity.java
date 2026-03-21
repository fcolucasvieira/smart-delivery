package com.fcolucasvieira.smartdelivery.modules.customers;

import com.fcolucasvieira.smartdelivery.modules.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "customers")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Column(nullable = false)
    private String email;

    private String address;

    private String zipCode;

    @Column(name = "user_id")
    private UUID userId;

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    // Construtor para CustomerMapper.toEntity()
    public CustomerEntity(String name, String phone, String zipCode, String email) {
        this.name = name;
        this.phone = phone;
        this.zipCode = zipCode;
        this.email = email;
    }
}


