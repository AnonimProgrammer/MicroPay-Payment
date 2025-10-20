package com.micropay.payment.service.messaging.wallet;

import com.micropay.payment.config.RabbitConstants;
import com.micropay.payment.dto.wallet.credit.WalletCreditFailedEvent;
import com.micropay.payment.dto.wallet.credit.WalletCreditedEvent;
import com.micropay.payment.dto.wallet.debit.WalletDebitFailedEvent;
import com.micropay.payment.dto.wallet.debit.WalletDebitedEvent;
import com.micropay.payment.dto.wallet.refund.WalletRefundedEvent;
import com.micropay.payment.service.coordinator.PaymentCoordinatorService;
import com.micropay.payment.service.messaging.inbox.InboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletMessageListener {

    private final PaymentCoordinatorService paymentCoordinatorService;
    private final InboxService inboxService;

    @RabbitListener(queues = RabbitConstants.WALLET_DEBITED_QUEUE)
    public void listenToWalletDebitedEvent(WalletDebitedEvent event) {
        log.info("Listening to WalletDebitedEvent: {}", event);

        try {
            inboxService.checkInbox(event.eventId());
        }  catch (Exception exception) { return; }
        paymentCoordinatorService.handleWalletDebitedEvent(event);
    }

    @RabbitListener(queues = RabbitConstants.WALLET_CREDITED_QUEUE)
    public void listenToWalletCreditedEvent(WalletCreditedEvent event) {
        log.info("Listening to WalletCreditedEvent: {}", event);

        try {
            inboxService.checkInbox(event.eventId());
        }  catch (Exception exception) { return; }
        paymentCoordinatorService.handleWalletCreditedEvent(event);
    }

    @RabbitListener(queues = RabbitConstants.WALLET_DEBIT_FAILED_QUEUE)
    public void listenToWalletDebitFailedEvent(WalletDebitFailedEvent event) {
        log.info("Listening to WalletDebitFailedEvent: {}", event);

        try {
            inboxService.checkInbox(event.eventId());
        }  catch (Exception exception) { return; }
        paymentCoordinatorService.handleWalletDebitFailedEvent(event);
    }

    @RabbitListener(queues = RabbitConstants.WALLET_CREDIT_FAILED_QUEUE)
    public void listenToWalletCreditFailedEvent(WalletCreditFailedEvent event) {
        log.info("Listening to WalletCreditFailedEvent: {}", event);

        try {
            inboxService.checkInbox(event.eventId());
        }  catch (Exception exception) { return; }
        paymentCoordinatorService.handleWalletCreditFailedEvent(event);
    }

    @RabbitListener(queues = RabbitConstants.WALLET_REFUNDED_QUEUE)
    public void listenToWalletRefundedEvent(WalletRefundedEvent event) {
        log.info("Listening to WalletRefundedEvent: {}", event);

        try {
            inboxService.checkInbox(event.eventId());
        }  catch (Exception exception) { return; }
        paymentCoordinatorService.handleWalletRefundedEvent(event);
    }

}
