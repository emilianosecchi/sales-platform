package com.esecchi.productinventory.repository;

import com.esecchi.productinventory.model.Stock;
import com.esecchi.productinventory.response.StockResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findFirstByProduct_IdAndQuantityGreaterThanEqualOrderByQuantityDesc(Long productId, Integer quantityIsGreaterThan);
    Optional<Stock> findByProduct_IdAndWarehouse_Id(Long productId, Long warehouseId);

    @Query(
    "SELECT new com.esecchi.productinventory.response.StockResponseDTO(" +
            "s.id, p.id,p.name, w.id,w.name, s.quantity)" +
            "FROM Stock s " +
            "JOIN s.product p " +
            "JOIN s.warehouse w " +
            "WHERE (:productId IS NULL OR p.id = :productId) AND " +
            "(:warehouseId IS NULL OR w.id = :warehouseId)"
    )
    List<StockResponseDTO> findStockFilteredBy(Long productId, Long warehouseId);
}