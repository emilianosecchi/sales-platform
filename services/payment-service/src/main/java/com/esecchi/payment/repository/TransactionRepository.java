package com.esecchi.payment.repository;

import com.esecchi.payment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Boolean existsByOrderId(Long orderId);
}
