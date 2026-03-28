package com.esecchi.productinventory.messaging.consumer;

import com.esecchi.common.event.order.OrderCancelledEvent;
import com.esecchi.common.event.order.OrderCreatedEvent;
import com.esecchi.common.event.productinventory.StockReservationStatus;
import com.esecchi.productinventory.exception.InsufficientStockException;
import com.esecchi.productinventory.messaging.producer.StockReservationEventProducer;
import com.esecchi.productinventory.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedEventConsumer {

    private final StockService stockService;
    private final StockReservationEventProducer stockReservationEventProducer;

    @KafkaListener(topics = "${spring.kafka.topics.order-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        try {
            stockService.reserveStock(orderCreatedEvent.orderId(), orderCreatedEvent.items());
            stockReservationEventProducer.publishStockReservationResult(orderCreatedEvent.orderId(), StockReservationStatus.RESERVED, "Stock reservado exitosamente");
        } catch (InsufficientStockException ex) {
            stockReservationEventProducer.publishStockReservationResult(orderCreatedEvent.orderId(), StockReservationStatus.INSUFFICIENT_STOCK, ex.getMessage());
        }
    }

    @KafkaListener(topics = "${spring.kafka.topics.order-cancelled}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderCancelledEvent(OrderCancelledEvent orderCancelledEvent) {
        stockService.releaseStockReserved(orderCancelledEvent.orderId());
    }

}

