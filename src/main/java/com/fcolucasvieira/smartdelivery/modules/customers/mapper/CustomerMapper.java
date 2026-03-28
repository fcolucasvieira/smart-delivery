package com.fcolucasvieira.smartdelivery.modules.customers.mapper;

import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerRequest;
import com.fcolucasvieira.smartdelivery.modules.customers.entity.CustomerEntity;

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
