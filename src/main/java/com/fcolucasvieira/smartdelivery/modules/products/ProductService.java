package com.fcolucasvieira.smartdelivery.modules.products;

import com.fcolucasvieira.smartdelivery.modules.products.dto.ListProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.dto.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<ListProductResponse> findAll() {
        // List<ProductEntity>
        var products = this.productRepository.findAll();
        // List<ListProductResponse>
        return ProductMapper.toListResponse(products);
    }
}
