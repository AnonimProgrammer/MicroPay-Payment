package com.micropay.payment.messaging.wallet;

import com.micropay.payment.config.RabbitConstants;
import com.micropay.payment.dto.wallet.credit.CreditWalletEvent;
import com.micropay.payment.dto.wallet.debit.DebitWalletEvent;
import com.micropay.payment.dto.wallet.refund.RefundWalletEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletMessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final static Logger LOGGER = LoggerFactory.getLogger(WalletMessagePublisher.class);

    public void publishDebitWalletEvent(DebitWalletEvent debitWalletEvent) {
        LOGGER.info("[WalletMessagePublisher] - Publishing DebitWalletEvent: {}", debitWalletEvent);
        rabbitTemplate.convertAndSend(
                RabbitConstants.WALLET_DEBIT_EXCHANGE,
                RabbitConstants.WALLET_DEBIT_ROUTING_KEY, debitWalletEvent
        );
    }

    public void publishCreditWalletEvent(CreditWalletEvent creditWalletEvent) {
        LOGGER.info("[WalletMessagePublisher] - Publishing CreditWalletEvent: {}", creditWalletEvent);
        rabbitTemplate.convertAndSend(
                RabbitConstants.WALLET_CREDIT_EXCHANGE,
                RabbitConstants.WALLET_CREDIT_ROUTING_KEY, creditWalletEvent
        );
    }

    public void publishRefundWalletEvent(RefundWalletEvent refundWalletEvent) {
        LOGGER.info("[WalletMessagePublisher] - Publishing RefundWalletEvent: {}", refundWalletEvent);
        rabbitTemplate.convertAndSend(
                RabbitConstants.WALLET_REFUND_EXCHANGE,
                RabbitConstants.WALLET_REFUND_ROUTING_KEY, refundWalletEvent
        );
    }

}
