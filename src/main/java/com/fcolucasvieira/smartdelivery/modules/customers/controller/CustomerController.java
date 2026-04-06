package com.fcolucasvieira.smartdelivery.modules.customers.controller;

import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerResponse;
import com.fcolucasvieira.smartdelivery.modules.customers.usecases.CreateCustomerUseCase;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;

    @PostMapping
    public ResponseEntity<CreateCustomerResponse> create(@RequestBody @Valid CreateCustomerRequest request){
        CreateCustomerResponse response = createCustomerUseCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
