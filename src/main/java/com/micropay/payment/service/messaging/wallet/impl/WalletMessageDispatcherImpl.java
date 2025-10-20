package com.micropay.payment.service.messaging.wallet.impl;

import com.micropay.payment.config.RabbitConstants;
import com.micropay.payment.dto.wallet.credit.CreditWalletEvent;
import com.micropay.payment.dto.wallet.debit.DebitWalletEvent;
import com.micropay.payment.dto.wallet.refund.RefundWalletEvent;
import com.micropay.payment.model.event.entity.BaseEvent;
import com.micropay.payment.service.messaging.outbox.OutboxService;
import com.micropay.payment.service.messaging.wallet.WalletMessageDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletMessageDispatcherImpl implements WalletMessageDispatcher {

    private final OutboxService outboxService;

    @Override
    public void publishDebitWalletEvent(DebitWalletEvent event) {
        log.info("Publishing DebitWalletEvent: {}", event);
        BaseEvent baseEvent = outboxService.saveBaseEvent(
                "DEBIT_WALLET",
                RabbitConstants.WALLET_DEBIT_EXCHANGE,
                RabbitConstants.WALLET_DEBIT_ROUTING_KEY, event
        );
        outboxService.publish(baseEvent);
    }

    @Override
    public void publishCreditWalletEvent(CreditWalletEvent event) {
        log.info("Publishing CreditWalletEvent: {}", event);
        BaseEvent baseEvent = outboxService.saveBaseEvent(
                "CREDIT_WALLET",
                RabbitConstants.WALLET_CREDIT_EXCHANGE,
                RabbitConstants.WALLET_CREDIT_ROUTING_KEY, event
        );
        outboxService.publish(baseEvent);
    }

    @Override
    public void publishRefundWalletEvent(RefundWalletEvent event) {
        log.info("Publishing RefundWalletEvent: {}", event);
        BaseEvent baseEvent = outboxService.saveBaseEvent(
                "REFUND_WALLET",
                RabbitConstants.WALLET_REFUND_EXCHANGE,
                RabbitConstants.WALLET_REFUND_ROUTING_KEY, event
        );
        outboxService.publish(baseEvent);
    }

}
