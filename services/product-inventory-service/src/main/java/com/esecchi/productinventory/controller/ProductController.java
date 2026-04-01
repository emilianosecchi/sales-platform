package com.esecchi.productinventory.controller;

import com.esecchi.productinventory.model.ProductCategory;
import com.esecchi.productinventory.request.CreateProductRequest;
import com.esecchi.productinventory.request.UpdateProductPriceRequest;
import com.esecchi.productinventory.response.ProductResponseDTO;
import com.esecchi.productinventory.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid CreateProductRequest request) {
        ProductResponseDTO productResponseDTO = productService.createProduct(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productResponseDTO.id())
                .toUri();
        return ResponseEntity.created(location).body(productResponseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}/price")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateProductPrice(@PathVariable Long id, @RequestBody @Valid UpdateProductPriceRequest request) {
        productService.updateProductPrice(id, request.price());
        return ResponseEntity.ok().body("El precio se ha actualizado correctamente.");
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(name = "max_price", required = false) @Positive BigDecimal maxPrice
    ) {
        return ResponseEntity.ok(productService.getAllProductsFiltered(category, maxPrice));
    }

}