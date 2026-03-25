package com.esecchi.common.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        Integer quantity,
        BigDecimal unitPriceAtPurchase
) {}
