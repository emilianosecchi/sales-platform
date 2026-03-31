package com.esecchi.payment.service;

import com.esecchi.common.event.order.OrderPaymentRequestedEvent;
import com.esecchi.common.model.payment.PaymentStatus;
import com.esecchi.payment.gateway.PaymentGatewaySimulator;
import com.esecchi.payment.model.Transaction;
import com.esecchi.payment.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final PaymentGatewaySimulator gatewaySimulator;

    @Transactional
    public void processPayment(OrderPaymentRequestedEvent event) {
        if (transactionRepository.existsByOrderId(event.orderId())) {
            log.warn("El pago para la orden {} ya está siendo procesado o fue finalizado.", event.orderId());
            return;
        }

        Transaction transaction = Transaction.builder()
                .orderId(event.orderId())
                .amount(event.totalPrice())
                .paymentMethod(event.paymentMethod())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.saveAndFlush(transaction);
        log.info("Transacción creada en estado: {} para la orden: {} - Se inicia la comunicación externa con el servicio de: {}",
                PaymentStatus.PENDING, event.orderId(), event.paymentMethod());

        gatewaySimulator.simulatePayment(transaction.getId());
    }

}
