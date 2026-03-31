package com.fcolucasvieira.smartdelivery.modules.products.usecases;

import com.fcolucasvieira.smartdelivery.core.exceptions.ProductAlreadyExists;
import com.fcolucasvieira.smartdelivery.modules.products.entity.ProductEntity;
import com.fcolucasvieira.smartdelivery.modules.products.repository.ProductRepository;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductRequest;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository repository;

    public CreateProductResponse execute(CreateProductRequest request) {
        this.repository.findByName(request.name())
                .ifPresent(product -> {
                    throw new ProductAlreadyExists("Product already exists");
                });

        ProductEntity product = ProductMapper.toEntity(request);

        this.repository.save(product);
        return ProductMapper.toResponse(product);
    }
}
