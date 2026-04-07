package com.fcolucasvieira.smartdelivery.modules.deliveryman.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateDeliveryManResponse (
        UUID id,
        String name,
        String document
){}
