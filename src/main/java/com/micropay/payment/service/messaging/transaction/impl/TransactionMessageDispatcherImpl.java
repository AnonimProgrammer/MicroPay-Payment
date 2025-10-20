package com.micropay.payment.service.messaging.transaction.impl;

import com.micropay.payment.config.RabbitConstants;
import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.dto.transaction.TransactionFailedEvent;
import com.micropay.payment.dto.transaction.TransactionSucceededEvent;
import com.micropay.payment.model.event.entity.BaseEvent;
import com.micropay.payment.service.messaging.outbox.OutboxService;
import com.micropay.payment.service.messaging.transaction.TransactionMessageDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionMessageDispatcherImpl implements TransactionMessageDispatcher {

    private final OutboxService outboxService;

    @Override
    public void publishInitiateTransactionEvent(InitiateTransactionEvent event) {
        log.info("Publishing InitiateTransactionEvent: {}", event);
        BaseEvent baseEvent = outboxService.saveBaseEvent(
                "INITIATE_TRANSACTION",
                RabbitConstants.TRANSACTION_EXCHANGE,
                RabbitConstants.TRANSACTION_INITIATE_ROUTING_KEY, event
        );
        outboxService.publish(baseEvent);
    }

    @Override
    public void publishTransactionSucceededEvent(TransactionSucceededEvent event) {
        log.info("Publishing TransactionSucceededEvent: {}", event);
        BaseEvent baseEvent = outboxService.saveBaseEvent(
                "TRANSACTION_SUCCEEDED",
                RabbitConstants.TRANSACTION_EXCHANGE,
                RabbitConstants.TRANSACTION_SUCCEEDED_ROUTING_KEY, event
        );
        outboxService.publish(baseEvent);
    }

    @Override
    public void publishTransactionFailedEvent(TransactionFailedEvent event) {
        log.info("Publishing TransactionFailedEvent: {}", event);
        BaseEvent baseEvent = outboxService.saveBaseEvent(
                "TRANSACTION_FAILED",
                RabbitConstants.TRANSACTION_EXCHANGE,
                RabbitConstants.TRANSACTION_FAILED_ROUTING_KEY, event
        );
        outboxService.publish(baseEvent);
    }

}
