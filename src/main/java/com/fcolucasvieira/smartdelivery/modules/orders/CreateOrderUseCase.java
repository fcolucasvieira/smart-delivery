package com.fcolucasvieira.smartdelivery.modules.orders;

import com.fcolucasvieira.smartdelivery.modules.customers.CustomerEntity;
import com.fcolucasvieira.smartdelivery.modules.customers.CustomerRepository;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderRequest;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderResponse;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.OrderMapper;
import com.fcolucasvieira.smartdelivery.modules.users.UserEntity;
import com.fcolucasvieira.smartdelivery.modules.users.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateOrderUseCase {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private CustomerRepository customerRepository;

    public CreateOrderUseCase(OrderRepository orderRepository, UserRepository userRepository, CustomerRepository customerRepository){
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    public CreateOrderResponse execute(CreateOrderRequest createOrderRequest) {
        // Instancia o username logado através do contexto de segurança da aplicação (Auth Basic)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // .get() - Assume que a busca feita, existe
        // Instancia userEntity através de username
        UserEntity userEntity = this.userRepository.findByUsername(username).get();

        // Através de userEntity, instancia customerEntity
        CustomerEntity customerEntity = this.customerRepository.findByUserId(userEntity.getId()).get();

        // Com createOrderRequest(List<UUID> pedidosIds) e customerEntity, mapeamos uma orderEntity e salvamos
        OrderEntity orderEntity = OrderMapper.toEntity(createOrderRequest, customerEntity.getId());
        this.orderRepository.save(orderEntity);
        return new CreateOrderResponse(orderEntity.getId(), orderEntity.getStatus().toString());
    }
}
