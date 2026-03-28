package com.fcolucasvieira.smartdelivery.modules.products.usecases;

import com.fcolucasvieira.smartdelivery.modules.products.entity.ProductEntity;
import com.fcolucasvieira.smartdelivery.modules.products.repository.ProductRepository;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductRequest;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.mapper.ProductMapper;
import org.springframework.stereotype.Service;

@Service
public class CreateProductUseCase {

    private ProductRepository productRepository;

    public CreateProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public CreateProductResponse execute(CreateProductRequest createProductRequest) {
        ProductEntity productEntity = ProductMapper.toEntity(createProductRequest);

        this.productRepository.save(productEntity);
        return ProductMapper.toResponse(productEntity);
    }
}
