package com.fcolucasvieira.smartdelivery.modules.orders;

import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderRequest;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private CreateOrderUseCase createOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase){
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping("/")
    public CreateOrderResponse create(@RequestBody CreateOrderRequest createOrderRequest){
        return this.createOrderUseCase.execute(createOrderRequest);
    }
}
