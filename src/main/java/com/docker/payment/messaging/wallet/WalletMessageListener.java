package com.docker.payment.messaging.wallet;

import com.docker.payment.dto.wallet.credit.WalletCreditFailedEvent;
import com.docker.payment.dto.wallet.credit.WalletCreditedEvent;
import com.docker.payment.dto.wallet.debit.WalletDebitFailedEvent;
import com.docker.payment.dto.wallet.debit.WalletDebitedEvent;
import com.docker.payment.dto.wallet.refund.WalletRefundedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WalletMessageListener {

    private final static Logger logger = LoggerFactory.getLogger(WalletMessageListener.class);

    @RabbitListener(queues = "${rabbitmq.queue.wallet.debited}")
    public void listenToWalletDebitedEvent(WalletDebitedEvent walletDebitedEvent) {
        logger.info("[WalletMessageListener] - Listening to WalletDebitedEvent: {}", walletDebitedEvent);
    }

    @RabbitListener(queues = "${rabbitmq.queue.wallet.credited}")
    public void listenToWalletCreditedEvent(WalletCreditedEvent walletCreditedEvent) {
        logger.info("[WalletMessageListener] - Listening to WalletCreditedEvent: {}", walletCreditedEvent);
    }

    @RabbitListener(queues = "${rabbitmq.queue.wallet.debitFailed}")
    public void listenToWalletDebitFailedEvent(WalletDebitFailedEvent walletDebitFailedEvent) {
        logger.info("[WalletMessageListener] - Listening to WalletDebitFailedEvent: {}", walletDebitFailedEvent);
    }

    @RabbitListener(queues = "${rabbitmq.queue.wallet.creditFailed}")
    public void listenToWalletCreditFailedEvent(WalletCreditFailedEvent walletCreditFailedEvent) {
        logger.info("[WalletMessageListener] - Listening to WalletCreditFailedEvent: {}", walletCreditFailedEvent);
    }

    @RabbitListener(queues = "${rabbitmq.queue.wallet.refunded}")
    public void listenToWalletRefundedEvent(WalletRefundedEvent walletRefundedEvent) {
        logger.info("[WalletMessageListener] - Listening to WalletRefundedEvent: {}", walletRefundedEvent);
    }
}
