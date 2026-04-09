package com.fcolucasvieira.smartdelivery.modules.deliveryman.usecases;

import com.fcolucasvieira.smartdelivery.core.exceptions.AlreadyExistsException;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.dto.CreateDeliveryManResponse;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.entity.DeliveryManEntity;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.mapper.DeliveryManMapper;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.repository.DeliveryManRepository;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.dto.CreateDeliveryManRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDeliveryManUseCase {
    private final DeliveryManRepository repository;

    public CreateDeliveryManResponse execute(CreateDeliveryManRequest request){
        validateDeliveryMan(request);

        DeliveryManEntity deliveryMan = DeliveryManMapper.toEntity(request);

        this.repository.save(deliveryMan);

        return DeliveryManMapper.toResponse(deliveryMan);
    }

    private void validateDeliveryMan(CreateDeliveryManRequest request) {
        this.repository.findByDocument(request.document())
                .ifPresent(deliveryMan -> {
                    throw new AlreadyExistsException("Delivery man already exists with document: " + request.document());
                });

        this.repository.findByPhone(request.phone())
                .ifPresent(deliveryMan -> {
                    throw new AlreadyExistsException("Delivery man already exists with phone: " + request.phone());
                });
    }

}
