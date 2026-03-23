package com.fcolucasvieira.smartdelivery.modules.orders;

import com.fcolucasvieira.smartdelivery.modules.deliveryman.DeliveryManEntity;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.DeliveryManRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompleteOrderUseCase {

    private OrderRepository orderRepository;
    private DeliveryManRepository deliveryManRepository;

    public CompleteOrderUseCase(OrderRepository orderRepository, DeliveryManRepository deliveryManRepository) {
        this.orderRepository = orderRepository;
        this.deliveryManRepository = deliveryManRepository;
    }

    @Transactional
    public void execute(UUID orderId){
        OrderEntity order = getOrder(orderId);

        validateOrder(order);

        completeOrder(order);

        releaseDeliveryMan(order);
    }

    private OrderEntity getOrder(UUID orderId){
        return this.orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found " + orderId));
    }

    private void validateOrder(OrderEntity order){
        if(order.getStatus() != StatusOrder.EM_ROTA)
            throw new IllegalStateException("Order must be in EM_ROTA to be completed");

        if(order.getDeliveryManId() == null)
            throw new IllegalStateException("Order has no delivery man assigned");
    }

    private void completeOrder(OrderEntity order){
        order.setStatus(StatusOrder.ENTREGUE);
        this.orderRepository.save(order);
    }

    private void releaseDeliveryMan(OrderEntity order){
        DeliveryManEntity deliveryMan = this.deliveryManRepository
                .findById(order.getDeliveryManId())
                .orElseThrow(() -> new IllegalArgumentException("Delivery man not found"));

        deliveryMan.setAvailable(true);
        this.deliveryManRepository.save(deliveryMan);
    }


}
