package com.esecchi.productinventory.repository;

import com.esecchi.productinventory.model.Product;
import com.esecchi.productinventory.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE " +
            "(:category IS NULL OR p.category = :category) AND" +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(p.enabled = true)"
    )
    List<Product> findByFilters(ProductCategory category, BigDecimal maxPrice);

    Optional<Product> findByNameAndCategory(String name, ProductCategory category);
}