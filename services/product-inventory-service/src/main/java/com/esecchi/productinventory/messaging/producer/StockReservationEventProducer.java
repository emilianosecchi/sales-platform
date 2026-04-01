package com.esecchi.productinventory.messaging.producer;

import com.esecchi.common.event.productinventory.StockReservationResultEvent;
import com.esecchi.common.event.productinventory.StockReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockReservationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.stock-reservation}")
    private String stockReservationTopic;

    public void publishStockReservationResult(Long orderId, StockReservationStatus status, String message) {
        StockReservationResultEvent event = new StockReservationResultEvent(orderId, status, message);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(stockReservationTopic, event);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Mensaje enviado exitosamente al tópico: {} | Partición: {} | Evento: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        event);
            } else {
                log.error("Error al enviar el mensaje: {}", ex.getMessage());
            }
        });
    }
}
