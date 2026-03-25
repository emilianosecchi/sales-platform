package com.esecchi.order.service;

import com.esecchi.common.dto.product.ProductItem;
import com.esecchi.common.model.order.OrderStatus;
import com.esecchi.order.exception.OrderNotFoundException;
import com.esecchi.order.mapper.OrderMapper;
import com.esecchi.order.model.Order;
import com.esecchi.order.model.OrderItem;
import com.esecchi.order.model.ProductSnapshot;
import com.esecchi.order.repository.OrderRepository;
import com.esecchi.order.request.CreateOrderRequest;
import com.esecchi.order.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductSnapshotService productSnapshotService;
    private final OrderMapper orderMapper;

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return orderMapper.toResponse(order);
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request, Long userId) {
        Order newOrder = Order.builder()
                .userId(userId)
                .paymentMethod(request.paymentMethod())
                .status(OrderStatus.WAITING_FOR_STOCK_RESERVATION)
                .orderDate(LocalDateTime.now())
                .orderItems(new ArrayList<>())
                .build();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (ProductItem itemRequest : request.items()) {
            BigDecimal unitPrice = productSnapshotService.getProductById(itemRequest.productId()).getPrice();

            OrderItem item = OrderItem.builder()
                    .productId(itemRequest.productId())
                    .quantity(itemRequest.quantity())
                    .unitPriceAtPurchase(unitPrice)
                    .order(newOrder)
                    .build();

            newOrder.addItem(item);

            totalPrice = totalPrice.add(
                    unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        newOrder.setTotalPrice(totalPrice);
        orderRepository.save(newOrder);

        // Publicar evento para el flujo de la Saga (Kafka)

        return orderMapper.toResponse(newOrder);
    }

}
