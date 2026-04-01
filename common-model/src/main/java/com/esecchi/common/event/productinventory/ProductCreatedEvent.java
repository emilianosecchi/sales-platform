package com.esecchi.common.event.productinventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductCreatedEvent(
        Long productId,
        BigDecimal price,
        String name,
        LocalDateTime timestamp
) {
}
