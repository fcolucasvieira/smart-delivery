package com.fcolucasvieira.smartdelivery.modules.deliveryman.controller;

import com.fcolucasvieira.smartdelivery.modules.deliveryman.dto.CreateDeliveryManRequest;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.dto.CreateDeliveryManResponse;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.usecases.CreateDeliveryManUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/deliverymen")
@RequiredArgsConstructor
public class DeliveryManController {
    private final CreateDeliveryManUseCase createDeliveryManUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateDeliveryManResponse> create(@RequestBody @Valid CreateDeliveryManRequest request){
        CreateDeliveryManResponse response = this.createDeliveryManUseCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
