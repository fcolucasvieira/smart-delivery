package com.fcolucasvieira.smartdelivery.modules.products.controller;

import com.fcolucasvieira.smartdelivery.modules.products.usecases.ListAllProductsUseCase;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductRequest;
import com.fcolucasvieira.smartdelivery.modules.products.dto.CreateProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.dto.ListProductResponse;
import com.fcolucasvieira.smartdelivery.modules.products.usecases.CreateProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(
            summary = "Create a new product",
            description = "Creates a new product. Only users with ADMIN role can access this endpoint."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateProductResponse> create(@RequestBody @Valid CreateProductRequest request){
        CreateProductResponse response = createProductUseCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "List all products",
            description = "Returns all available products with id, name, description, and price."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<ListProductResponse>> listAll() {
        return ResponseEntity.ok(this.listAllProductsUseCase.execute());
    }
}
