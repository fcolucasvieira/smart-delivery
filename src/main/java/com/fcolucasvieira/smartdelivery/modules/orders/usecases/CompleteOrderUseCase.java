package com.fcolucasvieira.smartdelivery.modules.orders.usecases;

import com.fcolucasvieira.smartdelivery.infra.exceptions.DeliveryManNotAssignedException;
import com.fcolucasvieira.smartdelivery.infra.exceptions.InvalidStatusOrderException;
import com.fcolucasvieira.smartdelivery.infra.exceptions.NotFoundException;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.entity.DeliveryManEntity;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.repository.DeliveryManRepository;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CompleteOrderResponse;
import com.fcolucasvieira.smartdelivery.modules.orders.entity.OrderEntity;
import com.fcolucasvieira.smartdelivery.modules.orders.mapper.OrderMapper;
import com.fcolucasvieira.smartdelivery.modules.orders.repository.OrderRepository;
import com.fcolucasvieira.smartdelivery.modules.orders.entity.enums.StatusOrder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompleteOrderUseCase {

    private final OrderRepository orderRepository;
    private final DeliveryManRepository deliveryManRepository;

    @Transactional
    public CompleteOrderResponse execute(UUID orderId){
        OrderEntity order = getOrder(orderId);

        validateOrder(order);

        completeOrder(order);

        releaseDeliveryMan(order);

        return OrderMapper.toCompleteResponse(order);
    }

    private OrderEntity getOrder(UUID orderId){
        return this.orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
    }

    private void validateOrder(OrderEntity order){
        if(order.getStatus() != StatusOrder.EM_ROTA)
            throw new InvalidStatusOrderException();

        if(order.getDeliveryManId() == null)
            throw new DeliveryManNotAssignedException();
    }

    private void completeOrder(OrderEntity order){
        order.setStatus(StatusOrder.ENTREGUE);
        this.orderRepository.save(order);
    }

    private void releaseDeliveryMan(OrderEntity order){
        DeliveryManEntity deliveryMan = this.deliveryManRepository
                .findById(order.getDeliveryManId())
                .orElseThrow(() -> new NotFoundException("Delivery man not found"));

        deliveryMan.setAvailable(true);
        this.deliveryManRepository.save(deliveryMan);
    }
}
