package com.esecchi.order.messaging.producer;

import com.esecchi.common.event.order.OrderCreatedEvent;
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
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(orderCreatedTopic, orderCreatedEvent);
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
}