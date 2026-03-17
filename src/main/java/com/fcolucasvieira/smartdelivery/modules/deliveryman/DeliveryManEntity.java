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

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isAvailable;

    private DeliveryManEntity(String name, String document, String phone, boolean isAvailable) {
        this.name = name;
        this.document = document;
        this.phone = phone;
        this.isAvailable = isAvailable;
    }

    // Annotation - @Builder
    public static class Builder {
        private String name;
        private String document;
        private String phone;
        private boolean isAvailable;

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

        public Builder isAvailable(boolean isAvailable){
            this.isAvailable = isAvailable;
            return this;
        }

        public DeliveryManEntity build(){
            return new DeliveryManEntity(name, document, phone, isAvailable);
        }
    }
}
