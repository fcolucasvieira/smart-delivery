package com.fcolucasvieira.smartdelivery.modules.deliveryman;

import com.fcolucasvieira.smartdelivery.modules.deliveryman.dto.CreateDeliveryManRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateDeliveryManUseCase {
    private DeliveryManRepository deliveryManRepository;

    public CreateDeliveryManUseCase(DeliveryManRepository deliveryManRepository) {
        this.deliveryManRepository = deliveryManRepository;
    }

    public UUID execute(CreateDeliveryManRequest request){
        this.deliveryManRepository.findByDocument(request.document())
                .ifPresent(deliveryMan -> {
                    throw new IllegalArgumentException("Entregador já cadastrado");
                });

        // Construção de entidade usando Builder para uma construção mais legível
        DeliveryManEntity deliveryManEntity = new DeliveryManEntity.Builder()
                .name(request.name())
                .document(request.document())
                .phone(request.phone())
                .isAvailiable(true)
                .build();

        this.deliveryManRepository.save(deliveryManEntity);

        return deliveryManEntity.getId();
    }
}
