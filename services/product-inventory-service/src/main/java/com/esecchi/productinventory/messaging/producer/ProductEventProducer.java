package com.esecchi.productinventory.messaging.producer;

import com.esecchi.common.event.productinventory.ProductCreatedEvent;
import com.esecchi.common.event.productinventory.ProductPriceUpdatedEvent;
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
public class ProductEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.product-price-updated}")
    private String productPriceUpdatedTopic;
    @Value("${spring.kafka.topics.product-created}")
    private String productCreatedTopic;

    public void publishEvent(String topic, Record event) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, event);
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

    public void publishProductPriceUpdatedEvent(ProductPriceUpdatedEvent event) {
        publishEvent(productPriceUpdatedTopic, event);
    }

    public void publishProductCreatedEvent(ProductCreatedEvent event) {
        publishEvent(productCreatedTopic, event);
    }

}
