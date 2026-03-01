package com.fcolucasvieira.smartdelivery.modules.customers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CreateCustomerUseCase createCustomerUseCase;

    public CustomerController(CreateCustomerUseCase createCustomerUseCase){
        this.createCustomerUseCase = createCustomerUseCase;
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody CustomerEntity customerEntity){
        try {
            createCustomerUseCase.execute(customerEntity);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }
}
