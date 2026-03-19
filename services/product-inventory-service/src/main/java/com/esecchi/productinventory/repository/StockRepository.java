package com.esecchi.productinventory.repository;

import com.esecchi.productinventory.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}