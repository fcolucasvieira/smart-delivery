package com.fcolucasvieira.smartdelivery.modules.customers.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateCustomerResponse (
        UUID id,
        String name,
        String email,
        String phone
){}
