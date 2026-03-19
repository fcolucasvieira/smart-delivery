package com.fcolucasvieira.smartdelivery.modules.orders;

import com.fcolucasvieira.smartdelivery.modules.deliveryman.DeliveryManEntity;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.DeliveryManRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


// Refletir sobre o caso de nenhum entregador está disponível para o caso
@Service
public class CreateDeliveryOrderUseCase {

    public OrderRepository orderRepository;
    public DeliveryManRepository deliveryManRepository;

    public CreateDeliveryOrderUseCase(DeliveryManRepository deliveryManRepository, OrderRepository orderRepository) {
        this.deliveryManRepository = deliveryManRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void execute(UUID orderId){
        // Instancia orderEntity através de orderId
        OrderEntity orderEntity = this.orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("Pedido não encontrado " + orderId);
                });

        // List de DeliveryManEntity onde parâmetro isAvailable = true
        List<DeliveryManEntity> deliveryManEntities = this.deliveryManRepository.findByIsAvailable(true);

        // Implementar Dead Letter Queue (DLQ) e Retry
        if(deliveryManEntities.isEmpty()){
            System.out.println("Nenhum deliveryman encontrado! " + orderId);
        }


        // Instancia um deliveryManEntity através da primeira ocorrência de deliveryManEntities
        DeliveryManEntity firstDeliveryManEntity = deliveryManEntities.getFirst();

        // Seta em orderEntity o deliveryManEntity (id) e status para EM_ROTA
        orderEntity.setDeliveryManId(firstDeliveryManEntity.getId());
        orderEntity.setStatus(StatusOrder.EM_ROTA);

        this.orderRepository.save(orderEntity);

        // Seta em firstDeliveryManEntity o parâmetro isAvailable = false
        firstDeliveryManEntity.setAvailable(false);

        this.deliveryManRepository.save(firstDeliveryManEntity);
    }
}
