package com.esecchi.payment.gateway;

import com.esecchi.common.event.payment.PaymentResultEvent;
import com.esecchi.common.model.payment.PaymentStatus;
import com.esecchi.payment.messaging.producer.PaymentEventProducer;
import com.esecchi.payment.model.Transaction;
import com.esecchi.payment.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentGatewaySimulator {

    private final TransactionRepository transactionRepository;
    private final PaymentEventProducer paymentEventProducer;

    private static final String[] ERROR_MESSAGES = {
            "SALDO INSUFICIENTE", "NÚMERO DE TARJETA INVÁLIDO", "TARJETA EXPIRADA",
            "CÓDIGO DE SEGURIDAD INVÁLIDO", "LÍMITE DIARIO EXCEDIDO", "TRANSACCIÓN NO AUTORIZADA"
    };

    private static final double SUCCESS_RATE = 0.70; // (70% de probabilidades de éxito)

    @Async
    @Transactional
    public void simulatePayment(Long transactionId) {
        log.info("Iniciando proceso de pago asincrónico para la transacción: {}", transactionId);
        try {
            long millis = ThreadLocalRandom.current().nextLong(8000, 15000);
            Thread.sleep(millis);

            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new RuntimeException("No fue posible encontrar la transacción con id: " + transactionId));


            boolean success = Math.random() < SUCCESS_RATE;
            transaction.setStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);

            transactionRepository.save(transaction);
            PaymentResultEvent event = new PaymentResultEvent(
                    transaction.getOrderId(),
                    success,
                    success ? "Pago procesado correctamente." : getRandomErrorMessage());

            paymentEventProducer.publishPaymentResult(event);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String getRandomErrorMessage() {
        return ERROR_MESSAGES[ThreadLocalRandom.current().nextInt(ERROR_MESSAGES.length)];
    }
}