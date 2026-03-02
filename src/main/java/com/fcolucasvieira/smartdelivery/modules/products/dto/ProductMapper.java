package com.fcolucasvieira.smartdelivery.modules.products.dto;

import com.fcolucasvieira.smartdelivery.modules.products.ProductEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductEntity requestToEntity(CreateProductRequest createProductRequest) {
        return new ProductEntity(
                createProductRequest.code(),
                createProductRequest.name(),
                createProductRequest.description(),
                createProductRequest.price());
    }

    public static CreateProductResponse entityToResponse(ProductEntity productEntity) {
        return new CreateProductResponse(
                productEntity.getCode(),
                productEntity.getName(),
                productEntity.getId());
    }

    public static List<ListProductResponse> toResponse(List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(product -> new ListProductResponse(
                        product.getCode(),
                        product.getDescription(),
                        product.getName(),
                        product.getPrice()
                ))
                .collect(Collectors.toList());
    }
}
