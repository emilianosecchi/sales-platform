package com.esecchi.common.model.order;

public enum OrderStatus {
    WAITING_FOR_STOCK_RESERVATION,
    WAITING_FOR_PAYMENT,
    COMPLETED,
    WAITING_FOR_STOCK_RELEASE,
    CANCELLED
}