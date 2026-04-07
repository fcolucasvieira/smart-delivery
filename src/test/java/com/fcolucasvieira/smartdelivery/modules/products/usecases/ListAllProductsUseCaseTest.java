package com.fcolucasvieira.smartdelivery.modules.products.usecases;

import com.fcolucasvieira.smartdelivery.modules.products.dto.ListProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.entity.ProductEntity;
import com.fcolucasvieira.smartdelivery.modules.products.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListAllProductsUseCaseTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ListAllProductsUseCase useCase;

    @Test
    @DisplayName("Should list products successfully")
    void Success() {
        // Arrange
        List<ProductEntity> products = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            products.add(
                    ProductEntity.builder()
                    .name("Product " + i)
                    .description("Description product " + i)
                    .price(new BigDecimal(10))
                    .build()
            );
        }

        when(this.repository.findAll()).thenReturn(products);

        // Act
        List<ListProductResponse> result = useCase.execute();

        // Assert
        assertAll(
                () -> assertEquals(5, result.size()),
                () -> assertEquals("Product 0", result.getFirst().name()),
                () -> assertEquals("Description product 0", result.getFirst().description()),
                () -> assertEquals(new BigDecimal("10"), result.getFirst().price())
        );

        verify(this.repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void ListEmpty() {
        // Arrange
        when(this.repository.findAll()).thenReturn(List.of());

        // Act
        List<ListProductResponse> result = useCase.execute();

        assertTrue(result.isEmpty());
        verify(this.repository, times(1)).findAll();
    }
}