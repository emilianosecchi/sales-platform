package com.esecchi.notification.messaging;

import com.esecchi.common.event.order.OrderCancelledEvent;
import com.esecchi.common.event.order.OrderCompletedEvent;
import com.esecchi.common.event.user.UserCreatedEvent;
import com.esecchi.common.event.user.UserMailUpdatedEvent;
import com.esecchi.notification.service.UserCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final UserCacheService userCacheService;

    @KafkaListener(topics = "${spring.kafka.topics.user-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        userCacheService.setUserEmail(event.userId(), event.email());
        log.info("Simulando envío de email al usuario con email {}", event.email());
    }

    @KafkaListener(topics = "${spring.kafka.topics.user-email-updated}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserMailUpdatedEvent(UserMailUpdatedEvent event) {
        userCacheService.setUserEmail(event.userId(), event.newEmail());
    }

    @KafkaListener(topics = "${spring.kafka.topics.order-cancelled}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderCancelledEvent(OrderCancelledEvent event) {
        userCacheService.getUserEmail(event.userId()).ifPresentOrElse(
                email -> log.info("La orden con id: {} fue CANCELADA con el siguiente motivo: {} - Simulando envío de mail al correo: {}", event.orderId(), event.message(), email),
                () -> log.error("No se encontró email para el usuario {}", event.userId())
        );
    }

    @KafkaListener(topics = "${spring.kafka.topics.order-completed}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderCompletedEvent(OrderCompletedEvent event) {
        userCacheService.getUserEmail(event.userId()).ifPresentOrElse(
                email -> log.info("La orden con id: {} se encuentra COMPLETADA - Simulando envío de mail al correo: {}", event.orderId(), email),
                () -> log.error("No se encontró email para el usuario {}", event.userId())
        );
    }

}