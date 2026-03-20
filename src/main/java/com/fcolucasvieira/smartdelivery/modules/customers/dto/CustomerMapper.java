package com.fcolucasvieira.smartdelivery.modules.customers.dto;

import com.fcolucasvieira.smartdelivery.modules.customers.CustomerEntity;

public class CustomerMapper {
    public static CustomerEntity toEntity(CreateCustomerRequest request){
        return new CustomerEntity(
                request.name(),
                request.phone(),
                request.zipCode(),
                request.email()
                );
    }
}
