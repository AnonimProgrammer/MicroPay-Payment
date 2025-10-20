package com.micropay.payment.service.messaging.transaction;

import com.micropay.payment.config.RabbitConstants;
import com.micropay.payment.dto.transaction.TransactionCreatedEvent;
import com.micropay.payment.service.coordinator.PaymentCoordinatorService;
import com.micropay.payment.service.messaging.inbox.InboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionMessageListener {

    private final PaymentCoordinatorService paymentCoordinatorService;
    private final InboxService inboxService;

    @RabbitListener(queues = RabbitConstants.TRANSACTION_CREATED_QUEUE)
    public void listenToTransactionCreatedEvent(TransactionCreatedEvent event) {
        log.info("Listening to TransactionCreatedEvent: {}", event);

        try {
            inboxService.checkInbox(event.eventId());
        }  catch (Exception exception) { return; }
        paymentCoordinatorService.handleTransactionCreatedEvent(event);
    }
}
