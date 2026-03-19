package com.esecchi.productinventory.service;

import com.esecchi.productinventory.mapper.ProductMapper;
import com.esecchi.productinventory.model.Product;
import com.esecchi.productinventory.repository.ProductRepository;
import com.esecchi.productinventory.request.CreateProductRequest;
import com.esecchi.productinventory.response.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
