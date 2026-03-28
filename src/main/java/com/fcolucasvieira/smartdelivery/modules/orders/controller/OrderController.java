package com.fcolucasvieira.smartdelivery.modules.orders.controller;

import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderRequest;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderResponse;
import com.fcolucasvieira.smartdelivery.modules.orders.usecases.CompleteOrderUseCase;
import com.fcolucasvieira.smartdelivery.modules.orders.usecases.CreateOrderUseCase;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private CreateOrderUseCase createOrderUseCase;
    private CompleteOrderUseCase completeOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase, CompleteOrderUseCase completeOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.completeOrderUseCase = completeOrderUseCase;
    }

    @PostMapping("/")
    public CreateOrderResponse create(@RequestBody CreateOrderRequest createOrderRequest){
        return this.createOrderUseCase.execute(createOrderRequest);
    }

    @PutMapping("/delivered/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delivered(@PathVariable UUID orderId){
        this.completeOrderUseCase.execute(orderId);
        return "Order delivered successfully!";
    }
}
