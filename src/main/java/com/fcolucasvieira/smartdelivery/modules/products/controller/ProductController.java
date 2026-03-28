package com.fcolucasvieira.smartdelivery.modules.products.controller;

import com.fcolucasvieira.smartdelivery.modules.products.usecases.ListAllProductsUseCase;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductRequest;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.mapper.ListProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.usecases.CreateProductUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    CreateProductUseCase createProductUseCase;
    ListAllProductsUseCase listAllProductsUseCase;

    public ProductController(CreateProductUseCase createProductUseCase, ListAllProductsUseCase listAllProductsUseCase){
        this.createProductUseCase = createProductUseCase;
        this.listAllProductsUseCase = listAllProductsUseCase;
    }


    // @PreAuthorize() - Útil para permitir/negar rota através de verificações iniciais
    // Nesse caso, a verificação inicial foi o usuário logado ter role ADMIN
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody CreateProductRequest createProductRequest){
        try {
            CreateProductResponse productCreated = this.createProductUseCase.execute(createProductRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(productCreated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
        }
    }

    @GetMapping("/")
    public List<ListProductResponse> findAll() {
        return this.listAllProductsUseCase.findAll();
    }
}
