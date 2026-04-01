package com.esecchi.payment.messaging.producer;

import com.esecchi.common.event.payment.PaymentResultEvent;
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
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.payment-result}")
    private String paymentResultTopic;

    public void publishPaymentResult(PaymentResultEvent event) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(paymentResultTopic, event);
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
