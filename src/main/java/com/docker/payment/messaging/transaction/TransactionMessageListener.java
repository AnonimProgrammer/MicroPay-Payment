package com.docker.payment.messaging.transaction;

import com.docker.payment.dto.transaction.TransactionCreatedEvent;
import com.docker.payment.service.PaymentCoordinatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionMessageListener {

    private final PaymentCoordinatorService paymentCoordinatorService;
    private final static Logger logger = LoggerFactory.getLogger(TransactionMessageListener.class);

    public TransactionMessageListener(PaymentCoordinatorService paymentCoordinatorService) {
        this.paymentCoordinatorService = paymentCoordinatorService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.transaction.created}")
    public void listenToTransactionCreatedEvent(TransactionCreatedEvent transactionCreatedEvent) {
        logger.info("[TransactionMessageListener] - Listening to TransactionCreatedEvent: {}", transactionCreatedEvent);
        paymentCoordinatorService.handleTransactionCreatedEvent(transactionCreatedEvent);
    }
}
