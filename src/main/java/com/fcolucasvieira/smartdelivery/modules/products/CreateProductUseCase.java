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

    public CreateProductResponse execute(CreateProductRequest createProductRequest){
        this.productRepository.findByCode(createProductRequest.code())
                .ifPresent(product -> {
                    throw new IllegalArgumentException("Produto já existe");
                });

        ProductEntity productEntity = ProductMapper.requestToEntity(createProductRequest);

        this.productRepository.save(productEntity);
        return ProductMapper.entityToResponse(productEntity);
    }
}
