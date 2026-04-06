package com.esecchi.productinventory.response;

public record StockResponseDTO(
        Long stockId,
        Long productId,
        String productName,
        Long warehouseId,
        String warehouseName,
        Integer quantity
) {}