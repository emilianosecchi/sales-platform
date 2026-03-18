package com.esecchi.notification.messaging;

import com.esecchi.common.event.user.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationEventConsumer {

    @KafkaListener(topics = "${spring.kafka.topics.user-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
        log.info("Simulando envío de email al usuario {}", userCreatedEvent);
    }

}