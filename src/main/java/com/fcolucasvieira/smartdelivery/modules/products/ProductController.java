package com.fcolucasvieira.smartdelivery.modules.products;

import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductRequest;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.dto.ListProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    CreateProductUseCase createProductUseCase;
    ProductService productService;

    public ProductController(CreateProductUseCase createProductUseCase, ProductService productService){
        this.createProductUseCase = createProductUseCase;
        this.productService = productService;
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
        return this.productService.findAll();
    }
}
