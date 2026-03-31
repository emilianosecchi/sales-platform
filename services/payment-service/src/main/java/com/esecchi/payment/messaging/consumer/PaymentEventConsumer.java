package com.esecchi.payment.messaging.consumer;

import com.esecchi.common.event.order.OrderPaymentRequestedEvent;
import com.esecchi.payment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final TransactionService transactionService;

    @KafkaListener(topics = "${spring.kafka.topics.order-payment-requested}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderPaymentRequestedEvent(OrderPaymentRequestedEvent event) {
        log.info("Se recibió la solicitud de pago para la orden: {}", event.orderId());
        transactionService.processPayment(event);
    }

}