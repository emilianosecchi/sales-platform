package com.esecchi.common.event.productinventory;

import java.math.BigDecimal;

public record ProductCreatedEvent(
        Long productId,
        BigDecimal price,
        String name
) {
}
