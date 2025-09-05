package com.payment.messaging.transaction;

import com.payment.dto.transaction.InitiateTransactionEvent;
import com.payment.dto.transaction.TransactionFailedEvent;
import com.payment.dto.transaction.TransactionSucceededEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TransactionMessagePublisher {

    @Value("${rabbitmq.exchange.transaction}")
    private String transactionExchange;
    @Value("${rabbitmq.routing-key.transaction.initiate}")
    private String transactionInitiateRoutingKey;
    @Value("${rabbitmq.routing-key.transaction.succeeded}")
    private String transactionSucceededRoutingKey;
    @Value("${rabbitmq.routing-key.transaction.failed}")
    private String transactionFailedRoutingKey;

    private final RabbitTemplate rabbitTemplate;
    private final static Logger logger = LoggerFactory.getLogger(TransactionMessagePublisher.class);

    public TransactionMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishInitiateTransactionEvent(InitiateTransactionEvent initiateTransactionEvent) {
        logger.info("[TransactionMessagePublisher] - Publishing InitiateTransactionEvent: {}", initiateTransactionEvent);
        rabbitTemplate.convertAndSend(transactionExchange, transactionInitiateRoutingKey, initiateTransactionEvent);
    }

    public void publishTransactionSucceededEvent(TransactionSucceededEvent transactionSucceededEvent) {
        logger.info("[TransactionMessagePublisher] - Publishing TransactionSucceededEvent: {}", transactionSucceededEvent);
        rabbitTemplate.convertAndSend(transactionExchange, transactionSucceededRoutingKey, transactionSucceededEvent);
    }

    public void publishTransactionFailedEvent(TransactionFailedEvent transactionFailedEvent) {
        logger.info("[TransactionMessagePublisher] - Publishing TransactionFailedEvent: {}", transactionFailedEvent);
        rabbitTemplate.convertAndSend(transactionExchange, transactionFailedRoutingKey, transactionFailedEvent);
    }


}
