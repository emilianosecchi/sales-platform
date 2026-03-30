package com.esecchi.productinventory.repository;

import com.esecchi.productinventory.model.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockReservationRepository extends JpaRepository<StockReservation,Long> {
    List<StockReservation> findByOrderId(Long orderId);
    Boolean existsByOrderId(Long orderId);
}
