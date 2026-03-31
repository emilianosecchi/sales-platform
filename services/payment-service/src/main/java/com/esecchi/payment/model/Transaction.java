package com.esecchi.payment.model;

import com.esecchi.common.model.order.PaymentMethod;
import com.esecchi.common.model.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "_transactions")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long orderId;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false, updatable = false)
    private PaymentMethod paymentMethod;

    @Column(updatable = false)
    private LocalDateTime createdAt;

}
