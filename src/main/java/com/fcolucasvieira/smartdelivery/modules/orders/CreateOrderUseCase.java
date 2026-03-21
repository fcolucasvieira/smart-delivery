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
    public CreateOrderResponse execute(CreateOrderRequest createOrderRequest) {
        // Instancia o username logado através do contexto de segurança da aplicação (Auth Basic)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // .get() - Assume que a busca feita, existe
        // Instancia userEntity através de username
        UserEntity userEntity = this.userRepository.findByUsername(username).get();

        // Através de userEntity, instancia customerEntity
        CustomerEntity customerEntity = this.customerRepository.findByUserId(userEntity.getId()).get();

        OrderEntity order = new OrderEntity();
        order.setCustomerId(customerEntity.getId());

        order = orderRepository.save(order);

        List<OrderItemEntity> items = new ArrayList<>();

        for(OrderItemRequest itemRequest : createOrderRequest.items()){
            ProductEntity product = productRepository
                    .findById(itemRequest.productId())
                    .orElseThrow();

            OrderItemEntity item = new OrderItemEntity();
            item.setOrderId(order.getId());
            item.setProductId(product.getId());
            item.setQuantity(itemRequest.quantity());
            item.setPrice(product.getPrice());

            items.add(item);
        }

        order.setItems(items);
        orderRepository.save(order);

        // Publica um evento de "pedido criado" no RabbitMQ para processamento assíncrono por outros serviços.
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.QUEUE_ORDER_CREATED,
                new OrderEvent(order.getId().toString()));

        return new CreateOrderResponse(order.getId(), order.getStatus().toString());
    }
}
