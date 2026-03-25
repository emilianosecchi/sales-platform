package com.esecchi.order.repository;

import com.esecchi.order.model.ProductSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSnapshotRepository extends JpaRepository<ProductSnapshot, Long> {
}
