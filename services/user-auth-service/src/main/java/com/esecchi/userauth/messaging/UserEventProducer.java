package com.esecchi.userauth.messaging;

import com.esecchi.common.event.user.UserCreatedEvent;
import com.esecchi.common.event.user.UserMailUpdatedEvent;
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
public class UserEventProducer {

    @Value("${spring.kafka.topics.user-created}")
    private String userCreatedTopic;
    @Value("${spring.kafka.topics.user-email-updated}")
    private String userEmailUpdatedTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private void publishEvent(String topic, Record event) {
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

    public void publishUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
        publishEvent(userCreatedTopic, userCreatedEvent);
    }

    public void publishUserEmailUpdatedEvent(UserMailUpdatedEvent userMailUpdatedEvent) {
        publishEvent(userEmailUpdatedTopic, userMailUpdatedEvent);
    }
}