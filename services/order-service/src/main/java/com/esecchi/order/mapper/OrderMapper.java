package com.esecchi.order.mapper;

import com.esecchi.common.dto.order.OrderItemDTO;
import com.esecchi.common.event.order.OrderCreatedEvent;
import com.esecchi.order.response.OrderResponse;
import com.esecchi.order.model.Order;
import com.esecchi.order.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "orderItems", target = "items")
    OrderResponse toResponse(Order order);

    OrderItemDTO toItemDTO(OrderItem orderItem);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderItems", target = "items")
    OrderCreatedEvent toCreatedEvent(Order order);

}