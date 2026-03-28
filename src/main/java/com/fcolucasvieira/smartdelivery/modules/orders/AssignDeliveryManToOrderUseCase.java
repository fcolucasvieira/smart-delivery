package com.fcolucasvieira.smartdelivery.modules.orders;

import com.fcolucasvieira.smartdelivery.modules.deliveryman.DeliveryManEntity;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.DeliveryManRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class AssignDeliveryManToOrderUseCase {

    private OrderRepository orderRepository;
    private DeliveryManRepository deliveryManRepository;

    public AssignDeliveryManToOrderUseCase(DeliveryManRepository deliveryManRepository, OrderRepository orderRepository) {
        this.deliveryManRepository = deliveryManRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void execute(UUID orderId){
        OrderEntity order = getOrder(orderId);

        // Idempotência (caso de deliveryMan já designado à order)
        if(isAlreadyAssigned(order)) {
            return;
        }

        DeliveryManEntity deliveryMan = findFirstAvailableDeliveryMan();

        assignDeliveryMan(order, deliveryMan);

        updateDeliveryManAvailability(deliveryMan);
    }

    private OrderEntity getOrder(UUID orderId){
        return this.orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found " + orderId));
    }

    private boolean isAlreadyAssigned(OrderEntity order) {
        return order.getDeliveryManId() != null
                && order.getStatus() == StatusOrder.EM_ROTA;
    }

    private DeliveryManEntity findFirstAvailableDeliveryMan(){
        List<DeliveryManEntity> deliveryManEntities = this.deliveryManRepository.findByIsAvailable(true);

        if(deliveryManEntities.isEmpty())
            throw new IllegalStateException("No delivery man available");

        return deliveryManEntities.getFirst();
    }

    private void assignDeliveryMan(OrderEntity order, DeliveryManEntity deliveryMan){
        order.setDeliveryManId(deliveryMan.getId());
        order.setStatus(StatusOrder.EM_ROTA);

        orderRepository.save(order);
    }

    private void updateDeliveryManAvailability(DeliveryManEntity deliveryMan){
        deliveryMan.setAvailable(false);
        this.deliveryManRepository.save(deliveryMan);
    }
}
