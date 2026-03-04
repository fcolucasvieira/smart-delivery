package com.fcolucasvieira.smartdelivery.modules.orders;

import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderRequest;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderResponse;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.OrderMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateOrderUseCase {
    private OrderRepository orderRepository;

    public CreateOrderUseCase(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public CreateOrderResponse execute(CreateOrderRequest createOrderRequest) {
        OrderEntity orderEntity = OrderMapper.toEntity(createOrderRequest);
        this.orderRepository.save(orderEntity);
        return new CreateOrderResponse(orderEntity.getId(), orderEntity.getStatus().toString());
    }
}
