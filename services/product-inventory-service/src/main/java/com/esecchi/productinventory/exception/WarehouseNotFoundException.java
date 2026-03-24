package com.esecchi.productinventory.exception;

public class WarehouseNotFoundException extends RuntimeException {
    public WarehouseNotFoundException(Long id) {
        super("No se ha podido encontrar el deposito con el id: " + String.valueOf(id));
    }
}
