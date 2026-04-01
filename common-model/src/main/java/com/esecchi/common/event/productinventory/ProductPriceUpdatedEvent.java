package com.esecchi.common.event.productinventory;

import java.math.BigDecimal;

public record ProductPriceUpdatedEvent(
        Long productId,
        BigDecimal newPrice,
        BigDecimal oldPrice,
        String name
) {
}
