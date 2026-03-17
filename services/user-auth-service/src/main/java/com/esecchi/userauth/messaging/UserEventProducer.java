package com.esecchi.userauth.messaging;

import com.esecchi.common.event.user.UserCreatedEvent;
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
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(userCreatedTopic, userCreatedEvent);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Evento enviado exitosamente. Mensaje: {} | Partición: {} | Offset: {}",
                        result.getProducerRecord().value(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Error al enviar el evento: {}", ex.getMessage());
            }
        });
    }
}