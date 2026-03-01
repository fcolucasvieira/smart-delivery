package com.fcolucasvieira.smartdelivery.modules.products.dto;

import com.fcolucasvieira.smartdelivery.modules.products.ProductEntity;

public class ProductMapper {
    public static ProductEntity requestToEntity(CreateProductRequest createProductRequest) {
        return new ProductEntity(
                createProductRequest.code(),
                createProductRequest.name(),
                createProductRequest.description(),
                createProductRequest.price());
    }

    public static CreateProductResponse entityToResponse(ProductEntity productEntity) {
        return new CreateProductResponse(productEntity.getCode(),
                productEntity.getName(),
                productEntity.getId());
    }
}
