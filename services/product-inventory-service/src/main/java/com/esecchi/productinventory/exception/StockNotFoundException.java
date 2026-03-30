package com.esecchi.productinventory.exception;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(Long stockId) {
        super("No se pudo encontrar un registro de stock con id: " + String.valueOf(stockId));
    }
}
