package com.esecchi.productinventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock_reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private Long stockId;
    private Integer quantityReserved;

}
