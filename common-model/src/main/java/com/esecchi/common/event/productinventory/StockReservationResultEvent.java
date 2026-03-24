package com.esecchi.common.event.productinventory;

public record StockReservationResultEvent(
        Long orderId,
        StockReservationStatus status,
        String message
) {}