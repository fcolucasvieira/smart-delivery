package com.fcolucasvieira.smartdelivery;

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
    public CustomerEntity create(@RequestBody CustomerEntity customerEntity){
        createCustomerUseCase.execute(customerEntity);
        return customerEntity;
    }
}
