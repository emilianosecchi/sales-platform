package com.esecchi.common.event.payment;

import java.math.BigDecimal;

public record PaymentResultEvent(
        Long orderId,
        Boolean success,
        BigDecimal amount,
        String message
) {
}
