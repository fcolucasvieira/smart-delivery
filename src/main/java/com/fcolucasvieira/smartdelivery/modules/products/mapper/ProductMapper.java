package com.fcolucasvieira.smartdelivery.modules.products.mapper;

import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductRequest;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.dto.ListProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.entity.ProductEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductEntity toEntity(CreateProductRequest request) {
        return ProductEntity.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .build();
    }

    public static CreateProductResponse toResponse(ProductEntity entity) {
        return CreateProductResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static List<ListProductResponse> toListResponse(List<ProductEntity> list) {
        return list.stream()
                .map(product -> ListProductResponse.builder()
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
