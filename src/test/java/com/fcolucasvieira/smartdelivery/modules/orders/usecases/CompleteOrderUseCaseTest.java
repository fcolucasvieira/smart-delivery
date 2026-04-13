package com.fcolucasvieira.smartdelivery.modules.orders.usecases;

import com.fcolucasvieira.smartdelivery.infra.exceptions.DeliveryManNotAssignedException;
import com.fcolucasvieira.smartdelivery.infra.exceptions.InvalidStatusOrderException;
import com.fcolucasvieira.smartdelivery.infra.exceptions.NotFoundException;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.entity.DeliveryManEntity;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.repository.DeliveryManRepository;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CompleteOrderResponse;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompleteOrderUseCaseTest {

    @Mock private OrderRepository orderRepository;
    @Mock private DeliveryManRepository deliveryManRepository;

    @InjectMocks private CompleteOrderUseCase useCase;

    private UUID orderId;
    private UUID deliveryManId;

    @BeforeEach
    void setup() {
        orderId = UUID.randomUUID();
        deliveryManId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should complete order successfully")
    void success() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setId(orderId);
        order.setStatus(StatusOrder.EM_ROTA);
        order.setDeliveryManId(deliveryManId);

        DeliveryManEntity deliveryMan = new DeliveryManEntity();
        deliveryMan.setId(deliveryManId);
        deliveryMan.setAvailable(false);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(deliveryManRepository.findById(deliveryManId)).thenReturn(Optional.of(deliveryMan));

        // Act
        CompleteOrderResponse response = useCase.execute(orderId);

        // Assert
        assertNotNull(response);
        assertEquals(orderId, response.orderId());
        assertEquals(StatusOrder.ENTREGUE.toString(), response.status());

        ArgumentCaptor<OrderEntity> orderCaptor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderRepository).save(orderCaptor.capture());
        assertEquals(StatusOrder.ENTREGUE, orderCaptor.getValue().getStatus());

        ArgumentCaptor<DeliveryManEntity> deliveryCaptor = ArgumentCaptor.forClass(DeliveryManEntity.class);
        verify(deliveryManRepository).save(deliveryCaptor.capture());
        assertTrue(deliveryCaptor.getValue().isAvailable());
    }

    @Test
    @DisplayName("Should throw NotFoundException when order not found")
    void orderNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> useCase.execute(orderId));
    }

    @Test
    @DisplayName("Should throw InvalidStatusOrderException when order status is not EM_ROTA")
    void invalidStatus() {
        OrderEntity order = new OrderEntity();
        order.setId(orderId);
        order.setStatus(StatusOrder.CRIADO);
        order.setDeliveryManId(deliveryManId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(InvalidStatusOrderException.class, () -> useCase.execute(orderId));
    }

    @Test
    @DisplayName("Should throw DeliveryManNotAssignedException when order has no delivery man")
    void deliveryManNotAssigned() {
        OrderEntity order = new OrderEntity();
        order.setId(orderId);
        order.setStatus(StatusOrder.EM_ROTA);
        order.setDeliveryManId(null);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(DeliveryManNotAssignedException.class, () -> useCase.execute(orderId));
    }

    @Test
    @DisplayName("Should throw NotFoundException when delivery man not found")
    void deliveryManNotFound() {
        OrderEntity order = new OrderEntity();
        order.setId(orderId);
        order.setStatus(StatusOrder.EM_ROTA);
        order.setDeliveryManId(deliveryManId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(deliveryManRepository.findById(deliveryManId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(orderId));
    }
}
