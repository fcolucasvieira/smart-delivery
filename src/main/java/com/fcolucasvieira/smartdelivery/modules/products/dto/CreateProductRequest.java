package com.fcolucasvieira.smartdelivery.modules.products.dto;

public record CreateProductRequest(int code,
                                   String name,
                                   String description,
                                   double price) {}
