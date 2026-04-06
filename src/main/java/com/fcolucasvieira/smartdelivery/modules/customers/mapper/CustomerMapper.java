package com.fcolucasvieira.smartdelivery.modules.customers.mapper;

import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerRequest;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerResponse;
import com.fcolucasvieira.smartdelivery.modules.customers.entity.CustomerEntity;

public class CustomerMapper {
    public static CustomerEntity toEntity(CreateCustomerRequest request) {
        return CustomerEntity.builder()
                .name(request.name())
                .phone(request.phone())
                .email(request.email())
                .zipCode(request.zipCode())
                .build();
    }

    public static CreateCustomerResponse toResponse(CustomerEntity customer) {
        return CreateCustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .build();
    }
}
