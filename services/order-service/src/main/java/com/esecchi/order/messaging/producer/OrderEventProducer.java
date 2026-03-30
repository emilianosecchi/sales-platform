package com.esecchi.order.messaging.producer;

import com.esecchi.common.event.order.OrderCancelledEvent;
import com.esecchi.common.event.order.OrderCreatedEvent;
import com.esecchi.common.event.order.OrderPaymentRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventProducer {
    @Value("${spring.kafka.topics.order-created}")
    private String orderCreatedTopic;
    @Value("${spring.kafka.topics.order-cancelled}")
    private String orderCancelledTopic;
    @Value("${spring.kafka.topics.order-completed}")
    private String orderCompletedTopic;
    @Value("${spring.kafka.topics.order-payment-requested}")
    private String orderPaymentRequestedTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private void publishEvent(String topic, Record event) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, event);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Mensaje enviado exitosamente al tópico: {} | Partición: {} | Offset: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Error al enviar el mensaje: {}", ex.getMessage());
            }
        });
    }

    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        this.publishEvent(orderCreatedTopic, event);
    }

    public void publishOrderPaymentRequestEvent(OrderPaymentRequestedEvent event) {
        this.publishEvent(orderPaymentRequestedTopic, event);
    }

    public void publishOrderCancelledEvent(OrderCancelledEvent event) {
        this.publishEvent(orderCancelledTopic, event);
    }

    public void publishOrderCompletedEvent() {
        // TODO: Realizar la implementación
    }
}