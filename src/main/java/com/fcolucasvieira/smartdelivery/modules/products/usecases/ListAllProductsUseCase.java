package com.fcolucasvieira.smartdelivery.modules.products.usecases;

import com.fcolucasvieira.smartdelivery.modules.products.dto.ListProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.mapper.ProductMapper;
import com.fcolucasvieira.smartdelivery.modules.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListAllProductsUseCase {

    private final ProductRepository repository;

    public Page<ListProductResponse> execute(Pageable pageable) {
        return this.repository.findAll(pageable)
                .map(ProductMapper::toListResponse);
    }
}
