package com.esecchi.common.event.order;

import com.esecchi.common.model.order.PaymentMethod;

import java.math.BigDecimal;

public record OrderPaymentRequestedEvent(
        Long orderId,
        BigDecimal totalPrice,
        PaymentMethod paymentMethod,
        String paymentToken
) {
}
