package com.esecchi.common.event.order;

public record OrderPaymentFailedEvent(
        Long orderId
) {
}
