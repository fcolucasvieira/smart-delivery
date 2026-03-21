package com.fcolucasvieira.smartdelivery.modules.products.dto;

import java.math.BigDecimal;

public record CreateProductRequest(String name,
                                   String description,
                                   BigDecimal price) {}
