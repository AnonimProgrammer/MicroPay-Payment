package com.micropay.payment.messaging.transaction;

import com.micropay.payment.config.RabbitConstants;
import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.dto.transaction.TransactionFailedEvent;
import com.micropay.payment.dto.transaction.TransactionSucceededEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final static Logger logger = LoggerFactory.getLogger(TransactionMessagePublisher.class);

    public void publishInitiateTransactionEvent(InitiateTransactionEvent initiateTransactionEvent) {
        logger.info("[TransactionMessagePublisher] - Publishing InitiateTransactionEvent: {}", initiateTransactionEvent);
        rabbitTemplate.convertAndSend(
                RabbitConstants.TRANSACTION_EXCHANGE,
                RabbitConstants.TRANSACTION_INITIATE_ROUTING_KEY, initiateTransactionEvent
        );
    }

    public void publishTransactionSucceededEvent(TransactionSucceededEvent transactionSucceededEvent) {
        logger.info("[TransactionMessagePublisher] - Publishing TransactionSucceededEvent: {}", transactionSucceededEvent);
        rabbitTemplate.convertAndSend(
                RabbitConstants.TRANSACTION_EXCHANGE,
                RabbitConstants.TRANSACTION_SUCCEEDED_ROUTING_KEY, transactionSucceededEvent
        );
    }

    public void publishTransactionFailedEvent(TransactionFailedEvent transactionFailedEvent) {
        logger.info("[TransactionMessagePublisher] - Publishing TransactionFailedEvent: {}", transactionFailedEvent);
        rabbitTemplate.convertAndSend(
                RabbitConstants.TRANSACTION_EXCHANGE,
                RabbitConstants.TRANSACTION_FAILED_ROUTING_KEY, transactionFailedEvent
        );
    }


}
