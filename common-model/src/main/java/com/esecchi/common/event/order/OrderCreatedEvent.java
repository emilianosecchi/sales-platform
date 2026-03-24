package com.esecchi.common.event.order;

import com.esecchi.common.dto.product.ProductItem;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCreatedEvent(
        Long orderId,
        LocalDateTime createdAt,
        List<ProductItem> items
) {}