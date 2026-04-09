package com.fcolucasvieira.smartdelivery.modules.orders.usecases;

import com.fcolucasvieira.smartdelivery.configs.RabbitMQConfig;
import com.fcolucasvieira.smartdelivery.core.exceptions.NotFoundException;
import com.fcolucasvieira.smartdelivery.core.exceptions.OrderEmptyException;
import com.fcolucasvieira.smartdelivery.modules.customers.entity.CustomerEntity;
import com.fcolucasvieira.smartdelivery.modules.customers.repository.CustomerRepository;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderRequest;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderResponse;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.OrderEvent;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.OrderItemRequest;
import com.fcolucasvieira.smartdelivery.modules.orders.entity.OrderEntity;
import com.fcolucasvieira.smartdelivery.modules.orders.repository.OrderRepository;
import com.fcolucasvieira.smartdelivery.modules.products.entity.ProductEntity;
import com.fcolucasvieira.smartdelivery.modules.products.repository.ProductRepository;
import com.fcolucasvieira.smartdelivery.modules.users.entity.UserEntity;
import com.fcolucasvieira.smartdelivery.modules.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private ProductRepository productRepository;
    @Mock private RabbitTemplate rabbitTemplate;

    @InjectMocks private CreateOrderUseCase useCase;

    private UUID userId;
    private UUID customerId;
    private UUID productId;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        productId = UUID.randomUUID();

        // Mock de contexto de segurança
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName())
                .thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Should create order successfully")
    void Success() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(userId);
        when(this.userRepository.findByUsername("testUser"))
                .thenReturn(Optional.of(user));

        CustomerEntity customer = new CustomerEntity();
        customer.setId(customerId);
        when(this.customerRepository.findByUserId(userId))
                .thenReturn(Optional.of(customer));

        ProductEntity product = new ProductEntity();
        product.setId(productId);
        product.setPrice(new BigDecimal(100));
        when(this.productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        OrderEntity order = new OrderEntity();
        order.setId(UUID.randomUUID());
        when(this.orderRepository.save(any(OrderEntity.class)))
                .thenReturn(order);

        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new OrderItemRequest(productId, 3))
        );

        // Act
        CreateOrderResponse result = useCase.execute(request);

        // Assert
        assertNotNull(result);
        assertEquals(order.getId(), result.orderId());
        assertEquals(order.getStatus().toString() ,result.status());

        verify(orderRepository, times(2)).save(any(OrderEntity.class));
        verify(rabbitTemplate, times(1))
                .convertAndSend(eq(RabbitMQConfig.EXCHANGE), eq(RabbitMQConfig.ROUTING_KEY), any(OrderEvent.class));

    }

    @Test
    @DisplayName("Should throw OrderEmptyException when order no contain items")
    void OrderEmpty() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(userId);
        when(this.userRepository.findByUsername("testUser"))
                .thenReturn(Optional.of(user));

        CustomerEntity customer = new CustomerEntity();
        customer.setId(customerId);
        when(this.customerRepository.findByUserId(userId))
                .thenReturn(Optional.of(customer));

        OrderEntity order = new OrderEntity();
        order.setId(UUID.randomUUID());
        when(this.orderRepository.save(any(OrderEntity.class)))
                .thenReturn(order);

        CreateOrderRequest request = new CreateOrderRequest(List.of());

        // Assert
        assertThrows(OrderEmptyException.class , () -> useCase.execute(request));
    }

    @Test
    @DisplayName("Should throw NotFoundException when user not found")
    void UserNotFound() {
        // Arrange
        when(this.userRepository.findByUsername("testUser"))
                .thenReturn(Optional.empty());

        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new OrderItemRequest(productId, 3)));

        // Assert
        assertThrows(NotFoundException.class , () -> useCase.execute(request));
    }

    @Test
    @DisplayName("Should throw NotFoundException when customer not found")
    void CustomerNotFound() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(userId);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        when(customerRepository.findByUserId(userId)).thenReturn(Optional.empty());

        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new OrderItemRequest(productId, 1))
        );

        // Act & Assert
        assertThrows(NotFoundException.class, () -> useCase.execute(request));
    }


    @Test
    @DisplayName("Should throw NotFoundException when product not found")
    void ProductNotFound() {
        UserEntity user = new UserEntity();
        user.setId(userId);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        CustomerEntity customer = new CustomerEntity();
        customer.setId(customerId);
        when(customerRepository.findByUserId(userId)).thenReturn(Optional.of(customer));

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new OrderItemRequest(productId, 1))
        );

        assertThrows(NotFoundException.class, () -> useCase.execute(request));
    }

}