package com.fcolucasvieira.smartdelivery.modules.orders;

import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private CreateOrderUseCase createOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase){
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping("/")
    public String create(@RequestBody CreateOrderRequest createOrderRequest){
        this.createOrderUseCase.execute(createOrderRequest);
        return "Salvou";
    }
}
