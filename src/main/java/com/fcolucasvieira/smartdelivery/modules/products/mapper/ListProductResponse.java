package com.fcolucasvieira.smartdelivery.modules.products.mapper;

import java.math.BigDecimal;

public record ListProductResponse(String name,
                                  String description,
                                  BigDecimal price) {
}
