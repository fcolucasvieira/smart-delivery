package com.fcolucasvieira.smartdelivery.modules.products.usecases;

import com.fcolucasvieira.smartdelivery.core.exceptions.AlreadyExistsException;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductRequest;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.entity.ProductEntity;
import com.fcolucasvieira.smartdelivery.modules.products.mapper.ProductMapper;
import com.fcolucasvieira.smartdelivery.modules.products.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private CreateProductUseCase useCase;

    @Test
    @DisplayName("Should create product successfully when name does not exists")
    void Success() {
        // Arrange
        String name = "Product test";
        CreateProductRequest request = CreateProductRequest.builder()
                .name(name)
                .description("Description product test")
                .price(new BigDecimal(10))
                .build();

        ProductEntity product = ProductMapper.toEntity(request);

        when(this.repository.findByName(name))
                .thenReturn(Optional.empty());
        when(this.repository.save(any(ProductEntity.class)))
                .thenReturn(product);

        // Act
        CreateProductResponse result = useCase.execute(request);

        // Assert
        assertEquals(name, result.name());
        verify(this.repository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when product already exists")
    void NameAlreadyExists() {
        // Arrange
        String name = "Product test";
        CreateProductRequest request = CreateProductRequest.builder()
                .name(name)
                .description("Description product test")
                .price(new BigDecimal(10))
                .build();

        ProductEntity existingProduct = ProductMapper.toEntity(request);

        when(this.repository.findByName(name)).thenReturn(Optional.of(existingProduct));

        // Act & Assert
        assertThrows(AlreadyExistsException.class, () -> useCase.execute(request));
        verify(this.repository, never()).save(any(ProductEntity.class));
    }
}