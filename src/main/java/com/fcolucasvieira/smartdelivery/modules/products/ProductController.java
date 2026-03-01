package com.fcolucasvieira.smartdelivery.modules.products;

import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    CreateProductUseCase createProductUseCase;

    public ProductController(CreateProductUseCase createProductUseCase){
        this.createProductUseCase = createProductUseCase;
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody CreateProductRequest createProductRequest){
        try {
            var productCreated = this.createProductUseCase.execute(createProductRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(productCreated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
        }
    }
}
