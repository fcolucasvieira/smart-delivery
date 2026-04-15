package com.fcolucasvieira.smartdelivery.modules.customers.controller;

import com.fcolucasvieira.smartdelivery.infra.responses.ApiResponse;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerResponse;
import com.fcolucasvieira.smartdelivery.modules.customers.usecases.CreateCustomerUseCase;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerRequest;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "Create a new customer",
            description = "Registers a new customer in the system. This endpoint integrated with the external ViaCEP API to automatically fetch and validate address information based on the provided postal code (CEP)."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<CreateCustomerResponse>> create(@RequestBody @Valid CreateCustomerRequest request){
        CreateCustomerResponse response = createCustomerUseCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "Customer registered successfully"));
    }
}
