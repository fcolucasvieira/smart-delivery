package com.fcolucasvieira.smartdelivery.modules.orders;

import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderRequest;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderUseCase {
    public void execute(CreateOrderRequest createOrderRequest) {
        System.out.println(createOrderRequest.toString());
    }
}
