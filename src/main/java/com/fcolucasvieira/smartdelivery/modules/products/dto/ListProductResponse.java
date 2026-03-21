package com.fcolucasvieira.smartdelivery.modules.products.dto;

import java.math.BigDecimal;

public record ListProductResponse(String name,
                                  String description,
                                  BigDecimal price) {
}
