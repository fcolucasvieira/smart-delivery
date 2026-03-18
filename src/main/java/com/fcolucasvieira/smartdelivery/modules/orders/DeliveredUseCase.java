package com.fcolucasvieira.smartdelivery.modules.orders;

import com.fcolucasvieira.smartdelivery.modules.deliveryman.DeliveryManEntity;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.DeliveryManRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeliveredUseCase {

    private OrderRepository orderRepository;
    private DeliveryManRepository deliveryManRepository;

    public DeliveredUseCase(OrderRepository orderRepository, DeliveryManRepository deliveryManRepository) {
        this.orderRepository = orderRepository;
        this.deliveryManRepository = deliveryManRepository;
    }

    @Transactional
    public void execute(UUID orderId){
        // Instancia orderEntity através de orderId
        // Caso não encontre, lança excessão (EntityNotFoundException)
        OrderEntity orderEntity = this.orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Pedido não encontrado");
                });

        // Seta status de orderEntity como ENTREGUE e save()
        orderEntity.setStatus(StatusOrder.ENTREGUE);
        this.orderRepository.save(orderEntity);

        DeliveryManEntity deliveryMan = this.deliveryManRepository.findById(orderEntity.getDeliveryManId())
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("Entregador não cadastrado");
                });
        deliveryMan.setAvailable(true);
        this.deliveryManRepository.save(deliveryMan);


    }
}
