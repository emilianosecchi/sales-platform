package com.esecchi.productinventory.service;

import com.esecchi.common.event.productinventory.ProductPriceUpdatedEvent;
import com.esecchi.productinventory.exception.ProductNotFoundException;
import com.esecchi.productinventory.mapper.ProductMapper;
import com.esecchi.productinventory.messaging.producer.ProductEventProducer;
import com.esecchi.productinventory.model.Product;
import com.esecchi.productinventory.model.ProductCategory;
import com.esecchi.productinventory.repository.ProductRepository;
import com.esecchi.productinventory.request.CreateProductRequest;
import com.esecchi.productinventory.response.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductEventProducer productEventProducer;

    @Transactional
    public ProductResponseDTO createProduct(CreateProductRequest request) {
        Product product = productRepository.findByNameAndCategory(request.name(), request.category())
                .orElseGet(
                        () -> {
                            var newProduct = productMapper.toEntity(request);
                            productRepository.save(newProduct);
                            productEventProducer.publishProductCreatedEvent(productMapper.toEvent(newProduct));
                            return newProduct;
                        }
                );

        return productMapper.toResponse(product);
    }

    @Transactional
    public void updateProductPrice(Long id, BigDecimal newPrice) {
        var product = this.findProductEntityById(id);
        ProductPriceUpdatedEvent event = new ProductPriceUpdatedEvent(id, newPrice, product.getPrice(), product.getName(), LocalDateTime.now());
        product.setPrice(newPrice);
        productRepository.save(product);
        productEventProducer.publishProductPriceUpdatedEvent(event);
    }

    public ProductResponseDTO getProductById(Long id) {
        return productMapper.toResponse(
                findProductEntityById(id)
        );
    }

    public Boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    public Product getProductReferenceById(Long id) {
        return productRepository.getReferenceById(id);
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
