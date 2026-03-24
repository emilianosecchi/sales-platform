package com.esecchi.productinventory.repository;

import com.esecchi.productinventory.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findFirstByProduct_IdAndQuantityGreaterThanEqualOrderByQuantityDesc(Long productId, Integer quantityIsGreaterThan);
    Optional<Stock> findByProduct_IdAndWarehouse_Id(Long productId, Long warehouseId);
}