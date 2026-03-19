package com.esecchi.productinventory.repository;

import com.esecchi.productinventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}