package com.esecchi.order.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.esecchi.common.model.order.OrderStatus;
import com.esecchi.order.model.PaymentMethod;
import java.util.List;

public record OrderResponse(
        Long id,
        LocalDateTime orderDate,
        BigDecimal totalPrice,
        Long userId,
        OrderStatus status,
        PaymentMethod paymentMethod,
        List<OrderItemResponse> items
) {}