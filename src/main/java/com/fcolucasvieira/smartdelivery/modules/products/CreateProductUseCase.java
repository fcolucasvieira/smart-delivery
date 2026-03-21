package com.fcolucasvieira.smartdelivery.modules.products;

import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductRequest;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.dto.ProductMapper;
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
