package com.fcolucasvieira.smartdelivery.modules.orders.usecases;

import com.fcolucasvieira.smartdelivery.core.exceptions.NoDeliveryManAvailableException;
import com.fcolucasvieira.smartdelivery.core.exceptions.NotFoundException;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.entity.DeliveryManEntity;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.repository.DeliveryManRepository;
import com.fcolucasvieira.smartdelivery.modules.orders.entity.OrderEntity;
import com.fcolucasvieira.smartdelivery.modules.orders.entity.enums.StatusOrder;
import com.fcolucasvieira.smartdelivery.modules.orders.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignDeliveryManToOrderUseCaseTest {

    @Mock private OrderRepository orderRepository;
    @Mock private DeliveryManRepository deliveryManRepository;

    @InjectMocks AssignDeliveryManToOrderUseCase useCase;

    private UUID orderId;
    private UUID deliveryManId;

    @BeforeEach
    void setup() {
        orderId = UUID.randomUUID();
        deliveryManId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should assigned delivery man to order successfully")
    void Success() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setId(orderId);

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        DeliveryManEntity deliveryMan = new DeliveryManEntity();
        deliveryMan.setId(deliveryManId);
        deliveryMan.setAvailable(true);

        when(this.deliveryManRepository.findByIsAvailable(true))
                .thenReturn(List.of(deliveryMan));

        // Act
        useCase.execute(orderId);

        // Assert
        ArgumentCaptor<OrderEntity> captor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderRepository, times(1)).save(captor.capture());

        OrderEntity savedOrder = captor.getValue();
        assertEquals(deliveryManId, savedOrder.getDeliveryManId());
        assertEquals(StatusOrder.EM_ROTA, savedOrder.getStatus());

        assertFalse(deliveryMan.isAvailable());
    }


    @Test
    @DisplayName("Should throw NotFoundException when order not found")
    void orderNotFound() {
        // Arrange
        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        // Assert & Act
        assertThrows(NotFoundException.class, () -> useCase.execute(orderId));
    }

    @Test
    @DisplayName("Should return because delivery man already assigned to another order")
    void isAlreadyAssigned() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setId(orderId);
        order.setDeliveryManId(deliveryManId);
        order.setStatus(StatusOrder.EM_ROTA);

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // Act
        useCase.execute(orderId);

        // Assert
        verify(this.deliveryManRepository, never()).findByIsAvailable(true);
        verify(orderRepository, never()).save(any(OrderEntity.class));

        assertEquals(deliveryManId, order.getDeliveryManId());
        assertEquals(StatusOrder.EM_ROTA, order.getStatus());
    }

    @Test
    @DisplayName("Should throw NoDeliveryManAvailable when List<DeliveryManEntity> is empty")
    void deliveryManEntitiesEmpty() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setId(orderId);

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(this.deliveryManRepository.findByIsAvailable(true))
                .thenReturn(List.of());

        // Assert & Act
        assertThrows(NoDeliveryManAvailableException.class, () -> useCase.execute(orderId));

    }
}