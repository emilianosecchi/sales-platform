package com.esecchi.order.messaging.consumer;

import com.esecchi.common.event.payment.PaymentResultEvent;
import com.esecchi.common.event.productinventory.StockReservationResultEvent;
import com.esecchi.common.event.productinventory.StockReservationStatus;
import com.esecchi.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "${spring.kafka.topics.stock-reservation}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleStockReservationResultEvent(StockReservationResultEvent event) {
        switch (event.status()) {
            case RESERVED:
                orderService.confirmStockReservation(event.orderId());
                break;
            case INSUFFICIENT_STOCK:
                orderService.cancelOrderDueLackOfStock(event.orderId());
                break;
            case ERROR:
                orderService.cancelOrderDueInventoryError(event.orderId());
        }
    }

    @KafkaListener(topics = "${spring.kafka.topics.payment-result}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlePaymentResultEvent(PaymentResultEvent event) {
        if (event.success()) {
            orderService.confirmPayment(event.orderId());
        } else {
            orderService.cancelOrderDueFailedPayment(event.orderId(), event.message());
        }
    }

}
