package com.esecchi.order.service;

import com.esecchi.common.event.order.OrderCancelledEvent;
import com.esecchi.common.event.order.OrderCompletedEvent;
import com.esecchi.common.event.order.OrderPaymentRequestedEvent;
import com.esecchi.common.model.order.OrderStatus;
import com.esecchi.order.exception.OrderNotFoundException;
import com.esecchi.order.mapper.OrderMapper;
import com.esecchi.order.messaging.producer.OrderEventProducer;
import com.esecchi.order.model.Order;
import com.esecchi.order.model.OrderItem;
import com.esecchi.order.repository.OrderRepository;
import com.esecchi.order.request.CreateOrderRequest;
import com.esecchi.order.request.ItemRequest;
import com.esecchi.order.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductSnapshotService productSnapshotService;
    private final OrderMapper orderMapper;
    private final OrderEventProducer orderEventProducer;

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

        for (ItemRequest itemRequest : request.items()) {
            // FIXME: Corregir la obtención del precio del producto
            // BigDecimal unitPrice = productSnapshotService.getProductById(itemRequest.productId()).getPrice();
            BigDecimal unitPrice = BigDecimal.valueOf(10);

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

        orderEventProducer.publishOrderCreatedEvent(orderMapper.toCreatedEvent(newOrder));

        return orderMapper.toResponse(newOrder);
    }

    @Transactional
    public void confirmStockReservation(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        if (order.getStatus() == OrderStatus.WAITING_FOR_STOCK_RESERVATION) {
            order.setStatus(OrderStatus.WAITING_FOR_PAYMENT);
            orderRepository.save(order);
            orderEventProducer.publishOrderPaymentRequestEvent(
                    new OrderPaymentRequestedEvent(orderId, order.getTotalPrice(), order.getPaymentMethod(), order.getPaymentToken())
            );
        }
    }

    @Transactional
    public void confirmPayment(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        if (order.getStatus() == OrderStatus.WAITING_FOR_PAYMENT) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
            orderEventProducer.publishOrderCompletedEvent(
                    new OrderCompletedEvent(orderId, order.getUserId())
            );
        }
    }

    private void cancelOrder(Long orderId, OrderStatus orderStatus, String reason) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        if (order.getStatus() == OrderStatus.WAITING_FOR_STOCK_RESERVATION || order.getStatus() == OrderStatus.WAITING_FOR_PAYMENT) {
            order.setStatus(orderStatus);
            orderRepository.save(order);
            orderEventProducer.publishOrderCancelledEvent(
                    new OrderCancelledEvent(orderId, order.getUserId(), orderStatus, reason, LocalDateTime.now())
            );
        }
    }

    @Transactional
    public void cancelOrderDueLackOfStock(Long orderId) {
        this.cancelOrder(orderId, OrderStatus.CANCELLED_INSUFFICIENT_STOCK,
                "Alguno de los productos seleccionados no presentaba disponibilidad de stock en nuestros depósitos.");
    }

    @Transactional
    public void cancelOrderDueInventoryError(Long orderId) {
        this.cancelOrder(orderId, OrderStatus.CANCELLED_INVENTORY_ERROR,
                "Hubo un error inesperado en el inventario.");
    }

    @Transactional
    public void cancelOrderDueFailedPayment(Long orderId, String errorMessage) {
        this.cancelOrder(orderId, OrderStatus.CANCELLED_PAYMENT_FAILED, errorMessage);
    }

}
