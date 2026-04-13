package com.fcolucasvieira.smartdelivery.modules.orders.controller;

import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderRequest;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderResponse;
import com.fcolucasvieira.smartdelivery.modules.orders.usecases.CompleteOrderUseCase;
import com.fcolucasvieira.smartdelivery.modules.orders.usecases.CreateOrderUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final CompleteOrderUseCase completeOrderUseCase;

    @PostMapping
    public CreateOrderResponse create(@RequestBody @Valid CreateOrderRequest request){
        return this.createOrderUseCase.execute(request);
    }

    @PutMapping("/delivered/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delivered(@PathVariable UUID orderId){
        this.completeOrderUseCase.execute(orderId);
        return "Order delivered successfully!";
    }
}
