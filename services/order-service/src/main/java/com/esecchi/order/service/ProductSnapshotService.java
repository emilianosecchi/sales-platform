package com.esecchi.order.service;

import com.esecchi.order.exception.ProductNotFoundException;
import com.esecchi.order.model.ProductSnapshot;
import com.esecchi.order.repository.ProductSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSnapshotService {

    private final ProductSnapshotRepository productSnapshotRepository;

    public ProductSnapshot getProductById(Long id) {
        return productSnapshotRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

}
