package com.fcolucasvieira.smartdelivery.modules.orders;

import com.fcolucasvieira.smartdelivery.modules.deliveryman.DeliveryManEntity;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.DeliveryManRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CreateDeliveryOrderUseCase {

    public OrderRepository orderRepository;
    public DeliveryManRepository deliveryManRepository;

    public CreateDeliveryOrderUseCase(DeliveryManRepository deliveryManRepository, OrderRepository orderRepository) {
        this.deliveryManRepository = deliveryManRepository;
        this.orderRepository = orderRepository;
    }

    public void execute(UUID orderId){
        this.orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("Pedido não encontrado " + orderId);
                });

        List<DeliveryManEntity> deliveryManEntities = this.deliveryManRepository.findByIsAvailable(true);
        if(deliveryManEntities.isEmpty()){
            System.out.println("Nenhum deliveryman encontrado! " + orderId);
        }

        // Atribuir um deliveryMan a um pedido
    }
}
