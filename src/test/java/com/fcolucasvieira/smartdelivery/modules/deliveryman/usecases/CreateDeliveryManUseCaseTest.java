package com.fcolucasvieira.smartdelivery.modules.deliveryman.usecases;

import com.fcolucasvieira.smartdelivery.infra.exceptions.AlreadyExistsException;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.dto.CreateDeliveryManRequest;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.dto.CreateDeliveryManResponse;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.entity.DeliveryManEntity;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.mapper.DeliveryManMapper;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.repository.DeliveryManRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDeliveryManUseCaseTest {

    @Mock
    DeliveryManRepository repository;

    @InjectMocks
    CreateDeliveryManUseCase useCase;

    @Test
    @DisplayName("Should create delivery man successfully")
    void Success() {

        // Arrange
        String document = "123.456.789-00";
        String phone = "88999999999";
        CreateDeliveryManRequest request = CreateDeliveryManRequest.builder()
                .name("Delivery man test")
                .document(document)
                .phone(phone)
                .build();

        DeliveryManEntity deliveryMan = DeliveryManMapper.toEntity(request);

        when(this.repository.findByDocument(document))
                .thenReturn(Optional.empty());
        when(this.repository.findByPhone(phone))
                .thenReturn(Optional.empty());
        when(this.repository.save(any(DeliveryManEntity.class)))
                .thenReturn(deliveryMan);

        // Act
        CreateDeliveryManResponse result = this.useCase.execute(request);

        // Assert
        assertEquals("Delivery man test", result.name());
        assertEquals(document, result.document());
        assertTrue(deliveryMan.isAvailable());
        verify(this.repository, times(1)).save(any(DeliveryManEntity.class));
    }

    @Test
    @DisplayName("Should throw DeliveryManExists when document already exists")
    void DocumentAlreadyExists() {
        // Arrange
        String document = "123.456.789-00";
        String phone = "88999999999";
        CreateDeliveryManRequest request = CreateDeliveryManRequest.builder()
                .name("Delivery man test")
                .document(document)
                .phone(phone)
                .build();

        when(this.repository.findByDocument(document))
                .thenReturn(Optional.of(new DeliveryManEntity()));

        // Act & Assert
        assertThrows(AlreadyExistsException.class, () -> this.useCase.execute(request));
        verify(this.repository, never()).save(any(DeliveryManEntity.class));
    }

    @Test
    @DisplayName("Should throw DeliveryManExists when phone already exists")
    void PhoneAlreadyExists() {
        // Arrange
        String document = "123.456.789-00";
        String phone = "88999999999";
        CreateDeliveryManRequest request = CreateDeliveryManRequest.builder()
                .name("Delivery man test")
                .document(document)
                .phone(phone)
                .build();

        when(this.repository.findByPhone(phone))
                .thenReturn(Optional.of(new DeliveryManEntity()));

        // Act & Assert
        assertThrows(AlreadyExistsException.class, () -> this.useCase.execute(request));
        verify(this.repository, never()).save(any(DeliveryManEntity.class));
    }
}