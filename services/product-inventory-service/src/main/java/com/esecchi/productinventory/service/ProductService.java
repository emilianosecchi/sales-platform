package com.esecchi.productinventory.service;

import com.esecchi.productinventory.exception.ProductNotFoundException;
import com.esecchi.productinventory.mapper.ProductMapper;
import com.esecchi.productinventory.model.Product;
import com.esecchi.productinventory.model.ProductCategory;
import com.esecchi.productinventory.repository.ProductRepository;
import com.esecchi.productinventory.request.CreateProductRequest;
import com.esecchi.productinventory.response.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponseDTO createProduct(CreateProductRequest request) {
        Product product = productMapper.toEntity(request);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    public ProductResponseDTO getProductById(Long id) {
        return productMapper.toResponse(
                findProductEntityById(id)
        );
    }

    public List<ProductResponseDTO> getAllProductsFiltered(ProductCategory category, BigDecimal maxPrice) {
        return productRepository.findByFilters(category, maxPrice)
                .stream().map(productMapper::toResponse).toList();
    }

    private Product findProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException(id)
                );
    }

}
