package com.esecchi.order.service;

import com.esecchi.common.event.productinventory.ProductCreatedEvent;
import com.esecchi.common.event.productinventory.ProductPriceUpdatedEvent;
import com.esecchi.order.exception.ProductNotFoundException;
import com.esecchi.order.model.ProductSnapshot;
import com.esecchi.order.repository.ProductSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductSnapshotService {

    private final ProductSnapshotRepository productSnapshotRepository;

    public ProductSnapshot getProductById(Long id) {
        return productSnapshotRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public void createProductFromEvent(ProductCreatedEvent event) {
        if (!productSnapshotRepository.existsById(event.productId())) {
            this.createProduct(event.productId(), event.name(), event.price(), event.timestamp());
        }
    }

    @Transactional
    public void updateProductFromEvent(ProductPriceUpdatedEvent event) {
        productSnapshotRepository.findById(event.productId()).ifPresentOrElse(
            existingProduct -> {
                // Solo actualizo si el evento es posterior a la última actualización del producto
                if (event.timestamp().isAfter(existingProduct.getLastUpdatedAt())) {
                    existingProduct.setPrice(event.newPrice());
                    existingProduct.setLastUpdatedAt(event.timestamp());
                    existingProduct.setName(event.name());
                    productSnapshotRepository.save(existingProduct);
                }
            },
            () -> {
                // Si no existe, lo creo con los datos del update
                this.createProduct(event.productId(), event.name(),  event.newPrice(), event.timestamp());
            }
        );
    }

    private void createProduct(Long id, String name, BigDecimal price, LocalDateTime timestamp) {
        var product = ProductSnapshot.builder()
                .id(id)
                .name(name)
                .price(price)
                .lastUpdatedAt(timestamp)
                .build();
        productSnapshotRepository.save(product);
    }

}
