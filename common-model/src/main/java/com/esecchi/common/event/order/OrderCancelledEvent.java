package com.esecchi.common.event.order;

import java.time.LocalDateTime;

public record OrderCancelledEvent(
        Long orderId,
        LocalDateTime cancelledAt
) {}