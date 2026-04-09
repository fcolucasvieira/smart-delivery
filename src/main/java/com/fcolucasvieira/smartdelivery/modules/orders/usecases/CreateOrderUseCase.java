package com.fcolucasvieira.smartdelivery.modules.orders.usecases;

import com.fcolucasvieira.smartdelivery.configs.RabbitMQConfig;
import com.fcolucasvieira.smartdelivery.core.exceptions.NotFoundException;
import com.fcolucasvieira.smartdelivery.core.exceptions.OrderEmptyException;
import com.fcolucasvieira.smartdelivery.modules.customers.entity.CustomerEntity;
import com.fcolucasvieira.smartdelivery.modules.customers.repository.CustomerRepository;
import com.fcolucasvieira.smartdelivery.modules.orders.entity.OrderEntity;
import com.fcolucasvieira.smartdelivery.modules.orders.entity.OrderItemEntity;
import com.fcolucasvieira.smartdelivery.modules.orders.repository.OrderRepository;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderRequest;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderResponse;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.OrderEvent;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.OrderItemRequest;
import com.fcolucasvieira.smartdelivery.modules.products.entity.ProductEntity;
import com.fcolucasvieira.smartdelivery.modules.products.repository.ProductRepository;
import com.fcolucasvieira.smartdelivery.modules.users.entity.UserEntity;
import com.fcolucasvieira.smartdelivery.modules.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public CreateOrderResponse execute(CreateOrderRequest request) {
        CustomerEntity customer = getAuthenticatedCustomer();

        OrderEntity order = createOrder(customer);

        List<OrderItemEntity> items = buildOrderItems(order, request);

        order.setItems(items);
        order = this.orderRepository.save(order);

        publishEvent(order);

        return buildResponse(order);
    }

    private CustomerEntity getAuthenticatedCustomer(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return this.customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));
    }

    private OrderEntity createOrder(CustomerEntity customer){
        OrderEntity order = new OrderEntity();
        order.setCustomerId(customer.getId());

        return this.orderRepository.save(order);
    }

    private List<OrderItemEntity> buildOrderItems(OrderEntity order, CreateOrderRequest request) {
        if(request.items() == null || request.items().isEmpty()) {
            throw new OrderEmptyException();
        }

        List<OrderItemEntity> items = new ArrayList<>();

        for(OrderItemRequest itemRequest : request.items()){
            ProductEntity product = this.productRepository
                    .findById(itemRequest.productId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));

            OrderItemEntity item = OrderItemEntity.builder()
                    .orderId(order.getId())
                    .productId(product.getId())
                    .quantity(itemRequest.quantity())
                    .price(product.getPrice())
                    .build();

            items.add(item);
        }

        return items;
    }

    private void publishEvent(OrderEntity order){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                new OrderEvent(order.getId().toString())
        );
    }

    private CreateOrderResponse buildResponse(OrderEntity order){
        return CreateOrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().toString())
                .build();
    }
}
