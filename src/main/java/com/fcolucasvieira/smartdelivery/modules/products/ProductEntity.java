package com.fcolucasvieira.smartdelivery.modules.products;

import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table(name = "products")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int code;
    private String name;
    private String description;
    private double price;

    public ProductEntity(int code, String name, String description, double price){
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
