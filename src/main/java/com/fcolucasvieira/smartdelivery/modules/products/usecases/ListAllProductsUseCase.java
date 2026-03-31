package com.fcolucasvieira.smartdelivery.modules.products.usecases;

import com.fcolucasvieira.smartdelivery.modules.products.dto.ListProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.entity.ProductEntity;
import com.fcolucasvieira.smartdelivery.modules.products.mapper.ProductMapper;
import com.fcolucasvieira.smartdelivery.modules.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListAllProductsUseCase {
    private final ProductRepository repository;

    public List<ListProductResponse> execute() {
        List<ProductEntity> listProducts = this.repository.findAll();
        return ProductMapper.toListResponse(listProducts);
    }
}
