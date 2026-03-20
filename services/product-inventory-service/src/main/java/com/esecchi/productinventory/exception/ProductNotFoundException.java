package com.esecchi.productinventory.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("No se ha podido encontrar el producto con el id: " + String.valueOf(id));
    }
}
