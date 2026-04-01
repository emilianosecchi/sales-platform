package com.esecchi.order.messaging.consumer;

import com.esecchi.common.event.productinventory.ProductCreatedEvent;
import com.esecchi.common.event.productinventory.ProductPriceUpdatedEvent;
import com.esecchi.order.service.ProductSnapshotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductSnapshotEventConsumer {

    private final ProductSnapshotService productSnapshotService;

    @KafkaListener(topics = "${spring.kafka.topics.product-price-updated}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleProductPriceUpdatedEvent(ProductPriceUpdatedEvent event) {
        log.info("Se recibió el evento para actualizar el producto con nombre: {} e id: {}", event.name(), event.productId());
        productSnapshotService.updateProductFromEvent(event);
    }

    @KafkaListener(topics = "${spring.kafka.topics.product-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleProductCreatedEvent(ProductCreatedEvent event) {
        log.info("Se recibió el evento para crear el producto con nombre: {} e id: {}", event.name(), event.productId());
        productSnapshotService.createProductFromEvent(event);
    }

}
