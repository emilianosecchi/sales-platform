package com.esecchi.common.dto.order;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long productId,
        Integer quantity,
        BigDecimal unitPriceAtPurchase
) {}
