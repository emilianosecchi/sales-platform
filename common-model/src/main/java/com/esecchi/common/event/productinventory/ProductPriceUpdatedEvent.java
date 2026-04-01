package com.esecchi.common.event.productinventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductPriceUpdatedEvent(
        Long productId,
        BigDecimal newPrice,
        BigDecimal oldPrice,
        String name,
        LocalDateTime timestamp
) {
}
