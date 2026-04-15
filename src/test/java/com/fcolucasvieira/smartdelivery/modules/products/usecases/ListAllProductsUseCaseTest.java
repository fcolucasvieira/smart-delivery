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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

        Page<ProductEntity> page = new PageImpl<>(products, PageRequest.of(0,5), products.size());

        when(this.repository.findAll(any(Pageable.class))).thenReturn(page);

        // Act
        Page<ListProductResponse> result = useCase.execute(PageRequest.of(0,5));

        // Assert
        assertAll(
                () -> assertEquals(5, result.getTotalElements()),
                () -> assertEquals("Product 0", result.getContent().get(0).name()),
                () -> assertEquals("Description product 0", result.getContent().get(0).description()),
                () -> assertEquals(new BigDecimal("10"), result.getContent().get(0).price())
        );

        verify(this.repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void ListEmpty() {
        // Arrange
        Page<ProductEntity> emptyPage = Page.empty();
        when(this.repository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // Act
        Page<ListProductResponse> result = useCase.execute(PageRequest.of(0, 5));

        assertTrue(result.isEmpty());
        verify(this.repository, times(1)).findAll(any(Pageable.class));
    }
}