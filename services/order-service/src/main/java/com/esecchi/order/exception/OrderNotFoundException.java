package com.esecchi.order.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("No se pudo encontrar la orden con el id: " + String.valueOf(id));
    }
}
