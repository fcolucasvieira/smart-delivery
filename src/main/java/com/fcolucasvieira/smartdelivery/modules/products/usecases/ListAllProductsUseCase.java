package com.fcolucasvieira.smartdelivery.modules.products.usecases;

import com.fcolucasvieira.smartdelivery.modules.products.mapper.ListProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.mapper.ProductMapper;
import com.fcolucasvieira.smartdelivery.modules.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListAllProductsUseCase {
    private ProductRepository productRepository;

    public ListAllProductsUseCase(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<ListProductResponse> findAll() {
        // List<ProductEntity>
        var products = this.productRepository.findAll();
        // List<ListProductResponse>
        return ProductMapper.toListResponse(products);
    }
}
