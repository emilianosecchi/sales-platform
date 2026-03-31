package com.esecchi.common.event.payment;

public record PaymentResultEvent(
        Long orderId,
        Boolean success,
        String message
) { }