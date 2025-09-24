package com.micropay.payment.messaging.transaction;

import com.micropay.payment.config.RabbitConstants;
import com.micropay.payment.dto.transaction.TransactionCreatedEvent;
import com.micropay.payment.service.PaymentCoordinatorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMessageListener {

    private final PaymentCoordinatorService paymentCoordinatorService;
    private final static Logger logger = LoggerFactory.getLogger(TransactionMessageListener.class);

    @RabbitListener(queues = RabbitConstants.TRANSACTION_CREATED_QUEUE)
    public void listenToTransactionCreatedEvent(TransactionCreatedEvent transactionCreatedEvent) {
        logger.info("[TransactionMessageListener] - Listening to TransactionCreatedEvent: {}", transactionCreatedEvent);
        paymentCoordinatorService.handleTransactionCreatedEvent(transactionCreatedEvent);
    }
}
