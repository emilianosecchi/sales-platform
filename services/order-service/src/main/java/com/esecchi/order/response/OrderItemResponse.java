package com.esecchi.order.response;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        Integer quantity,
        BigDecimal unitPriceAtPurchase
) {
}
