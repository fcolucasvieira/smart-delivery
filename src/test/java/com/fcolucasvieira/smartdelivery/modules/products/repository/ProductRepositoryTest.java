package com.fcolucasvieira.smartdelivery.modules.products.repository;

import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductRequest;
import com.fcolucasvieira.smartdelivery.modules.products.entity.ProductEntity;
import com.fcolucasvieira.smartdelivery.modules.products.mapper.ProductMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    ProductRepository repository;

    @Test
    @DisplayName("Should get Product sucessfuly by name")
    void findByNameSucess() {
        // Arrange (Preparação)
        String name = "Product test";
        CreateProductRequest request = CreateProductRequest.builder()
                .name(name)
                .description("Description product test")
                .price(new BigDecimal(10))
                .build();

        ProductEntity product = ProductMapper.toEntity(request);
        this.repository.save(product);

        // Act (Ação)
        Optional<ProductEntity> result = this.repository.findByName(name);

        // Assert (Validação)
        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
    }

    @Test
    @DisplayName("Should not get Product from DB when product not exists")
    void findByNameError() {
        // Arrange (Preparação)
        String name = "Product test";

        // Act (Ação)
        Optional<ProductEntity> result = this.repository.findByName(name);

        // Assert (Validação)
        assertTrue(result.isEmpty());
    }
}