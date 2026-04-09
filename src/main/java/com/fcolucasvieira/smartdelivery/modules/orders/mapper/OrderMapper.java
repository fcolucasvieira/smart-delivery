package com.fcolucasvieira.smartdelivery.modules.orders.mapper;

import com.fcolucasvieira.smartdelivery.modules.orders.dto.CompleteOrderResponse;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderResponse;
import com.fcolucasvieira.smartdelivery.modules.orders.entity.OrderEntity;

public class OrderMapper {
    public static CreateOrderResponse toCreateResponse(OrderEntity entity){
        return CreateOrderResponse.builder()
                .orderId(entity.getId())
                .status(entity.getStatus().toString())
                .build();
    }

    public static CompleteOrderResponse toCompleteResponse(OrderEntity entity){
        return CompleteOrderResponse.builder()
                .orderId(entity.getId())
                .status(entity.getStatus().toString())
                .deliveryManId(entity.getDeliveryManId())
                .build();
    }
}
