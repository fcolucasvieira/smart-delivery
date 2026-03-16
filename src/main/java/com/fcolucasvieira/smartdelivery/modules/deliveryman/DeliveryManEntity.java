package com.fcolucasvieira.smartdelivery.modules.deliveryman;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table
@Entity(name = "delivery_men")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryManEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String document;

    private String phone;

    private DeliveryManEntity(String name, String document, String phone) {
        this.name = name;
        this.document = document;
        this.phone = phone;
    }

    // Annotation - @Builder
    public static class Builder {
        private String name;
        private String document;
        private String phone;

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder document(String document){
            this.document = document;
            return this;
        }

        public Builder phone(String phone){
            this.phone = phone;
            return this;
        }

        public DeliveryManEntity build(){
            return new DeliveryManEntity(name, document, phone);
        }
    }
}
