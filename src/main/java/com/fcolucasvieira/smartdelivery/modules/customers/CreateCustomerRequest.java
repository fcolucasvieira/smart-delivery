package com.fcolucasvieira.smartdelivery.modules.customers;

public record CreateCustomerRequest(String name,
                                    String phone,
                                    String email,
                                    String zipCode,
                                    String password) {
}
