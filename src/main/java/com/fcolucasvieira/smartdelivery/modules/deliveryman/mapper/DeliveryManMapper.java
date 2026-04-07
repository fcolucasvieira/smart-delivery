package com.fcolucasvieira.smartdelivery.modules.deliveryman.mapper;

import com.fcolucasvieira.smartdelivery.modules.deliveryman.dto.CreateDeliveryManRequest;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.dto.CreateDeliveryManResponse;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.entity.DeliveryManEntity;

public class DeliveryManMapper {
    public static DeliveryManEntity toEntity(CreateDeliveryManRequest request) {
        return DeliveryManEntity.builder()
                .name(request.name())
                .document(request.document())
                .phone(request.phone())
                .isAvailable(true)
                .build();
    }

    public static CreateDeliveryManResponse toResponse(DeliveryManEntity entity) {
        return CreateDeliveryManResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .document(entity.getDocument())
                .build();
    }
}
