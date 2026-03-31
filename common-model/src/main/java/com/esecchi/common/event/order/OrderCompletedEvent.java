package com.esecchi.common.event.order;

public record OrderCompletedEvent(
        Long orderId,
        Long userId
) {}