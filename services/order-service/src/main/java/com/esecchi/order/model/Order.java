package com.esecchi.order.model;

import com.esecchi.common.model.order.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDate;

    private BigDecimal totalPrice;

    @Column(nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    public void addItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

}