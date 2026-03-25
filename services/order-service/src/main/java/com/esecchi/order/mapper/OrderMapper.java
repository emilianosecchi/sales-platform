package com.esecchi.order.mapper;

import com.esecchi.order.response.OrderItemResponse;
import com.esecchi.order.response.OrderResponse;
import com.esecchi.order.model.Order;
import com.esecchi.order.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "orderItems", target = "items")
    OrderResponse toResponse(Order order);

    OrderItemResponse toItemResponse(OrderItem orderItem);
}