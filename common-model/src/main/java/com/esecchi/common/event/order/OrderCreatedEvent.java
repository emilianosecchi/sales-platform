package com.esecchi.common.event.order;

import com.esecchi.common.dto.order.OrderItemResponse;
import com.esecchi.common.model.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderCreatedEvent(
        Long orderId,
        LocalDateTime orderDate,
        BigDecimal totalPrice,
        OrderStatus status,
        List<OrderItemResponse> items
) {}