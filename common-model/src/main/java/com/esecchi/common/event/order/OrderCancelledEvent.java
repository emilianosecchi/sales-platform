package com.esecchi.common.event.order;

import com.esecchi.common.model.order.OrderStatus;

import java.time.LocalDateTime;

public record OrderCancelledEvent(
        Long orderId,
        Long userId,
        OrderStatus orderStatus,
        String message,
        LocalDateTime cancelledAt
) {}