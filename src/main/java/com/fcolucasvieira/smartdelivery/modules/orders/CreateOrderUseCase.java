package com.fcolucasvieira.smartdelivery.modules.orders;

import com.fcolucasvieira.smartdelivery.configs.RabbitMQConfig;
import com.fcolucasvieira.smartdelivery.modules.customers.CustomerEntity;
import com.fcolucasvieira.smartdelivery.modules.customers.CustomerRepository;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderRequest;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderResponse;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.OrderEvent;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.OrderItemRequest;
import com.fcolucasvieira.smartdelivery.modules.products.ProductEntity;
import com.fcolucasvieira.smartdelivery.modules.products.ProductRepository;
import com.fcolucasvieira.smartdelivery.modules.users.UserEntity;
import com.fcolucasvieira.smartdelivery.modules.users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreateOrderUseCase {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private RabbitTemplate rabbitTemplate;

    public CreateOrderUseCase(OrderRepository orderRepository, UserRepository userRepository, CustomerRepository customerRepository, ProductRepository productRepository, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

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
                .orElseThrow(() -> new RuntimeException("User not found"));

        return this.customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    private OrderEntity createOrder(CustomerEntity customer){
        OrderEntity order = new OrderEntity();
        order.setCustomerId(customer.getId());

        return this.orderRepository.save(order);
    }

    private List<OrderItemEntity> buildOrderItems(OrderEntity order, CreateOrderRequest request) {
        if(request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }

        List<OrderItemEntity> items = new ArrayList<>();
        for(OrderItemRequest itemRequest : request.items()){
            ProductEntity product = this.productRepository
                    .findById(itemRequest.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItemEntity item = new OrderItemEntity();
            item.setOrderId(order.getId());
            item.setProductId(product.getId());
            item.setQuantity(itemRequest.quantity());
            item.setPrice(product.getPrice());

            items.add(item);
        }

        return items;
    }

    private void publishEvent(OrderEntity order){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.QUEUE_ORDER_CREATED,
                new OrderEvent(order.getId().toString())
        );
    }

    private CreateOrderResponse buildResponse(OrderEntity order){
        return new CreateOrderResponse(
                order.getId(),
                order.getStatus().toString()
        );
    }
}
