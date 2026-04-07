package com.fcolucasvieira.smartdelivery.modules.products.controller;

import com.fcolucasvieira.smartdelivery.modules.products.usecases.ListAllProductsUseCase;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductRequest;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.dto.ListProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.usecases.CreateProductUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final ListAllProductsUseCase listAllProductsUseCase;

    // @PreAuthorize() - Útil para permitir/negar rota através de verificações iniciais
    // Nesse caso, a verificação inicial foi o usuário logado ter role ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateProductResponse> create(@RequestBody @Valid CreateProductRequest request){
        CreateProductResponse response = createProductUseCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ListProductResponse>> listAll() {
        return ResponseEntity.ok(this.listAllProductsUseCase.execute());
    }
}
