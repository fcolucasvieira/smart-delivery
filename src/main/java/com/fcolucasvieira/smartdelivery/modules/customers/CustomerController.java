package com.fcolucasvieira.smartdelivery.modules.customers;

import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CreateCustomerUseCase createCustomerUseCase;

    public CustomerController(CreateCustomerUseCase createCustomerUseCase){
        this.createCustomerUseCase = createCustomerUseCase;
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody CreateCustomerRequest createCustomerRequest){
        try {
            UUID userIdCreated = createCustomerUseCase.execute(createCustomerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("ID do usuário: " + userIdCreated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
        }
    }
}
