package com.fcolucasvieira.smartdelivery.modules.orders.controller;

import com.fcolucasvieira.smartdelivery.infra.responses.ApiResponse;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CompleteOrderResponse;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderRequest;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.CreateOrderResponse;
import com.fcolucasvieira.smartdelivery.modules.orders.usecases.CompleteOrderUseCase;
import com.fcolucasvieira.smartdelivery.modules.orders.usecases.CreateOrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final CompleteOrderUseCase completeOrderUseCase;

    @Operation(
            summary = "Create a new order",
            description = "Creates a new order for a customer. This endpoint requires JWT authentication. Order processing is asynchronous and handled via RabbitMQ with retry and DLQ mechanisms."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ApiResponse<CreateOrderResponse>> create(@RequestBody @Valid CreateOrderRequest request){
        CreateOrderResponse response = this.createOrderUseCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "Order created successfully"));
    }

    @Operation(
            summary = "Mark order as delivered",
            description = "Marks an existing order as delivered. Requires JWT authentication and can only be accessed by users with ADMIN role."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/delivered/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CompleteOrderResponse>> delivered(@PathVariable UUID orderId){
        CompleteOrderResponse response = this.completeOrderUseCase.execute(orderId);
        return ResponseEntity.ok(ApiResponse.success(response, "Order delivered successfully"));
    }
}
