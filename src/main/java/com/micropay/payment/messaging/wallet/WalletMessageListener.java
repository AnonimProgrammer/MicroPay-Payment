package com.micropay.payment.messaging.wallet;

import com.micropay.payment.config.RabbitConstants;
import com.micropay.payment.dto.wallet.credit.WalletCreditFailedEvent;
import com.micropay.payment.dto.wallet.credit.WalletCreditedEvent;
import com.micropay.payment.dto.wallet.debit.WalletDebitFailedEvent;
import com.micropay.payment.dto.wallet.debit.WalletDebitedEvent;
import com.micropay.payment.dto.wallet.refund.WalletRefundedEvent;
import com.micropay.payment.service.PaymentCoordinatorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletMessageListener {

    private final PaymentCoordinatorService paymentCoordinatorService;
    private final static Logger logger = LoggerFactory.getLogger(WalletMessageListener.class);


    @RabbitListener(queues = RabbitConstants.WALLET_DEBITED_QUEUE)
    public void listenToWalletDebitedEvent(WalletDebitedEvent walletDebitedEvent) {
        logger.info("[WalletMessageListener] - Listening to WalletDebitedEvent: {}", walletDebitedEvent);
        paymentCoordinatorService.handleWalletDebitedEvent(walletDebitedEvent);
    }

    @RabbitListener(queues = RabbitConstants.WALLET_CREDITED_QUEUE)
    public void listenToWalletCreditedEvent(WalletCreditedEvent walletCreditedEvent) {
        logger.info("[WalletMessageListener] - Listening to WalletCreditedEvent: {}", walletCreditedEvent);
        paymentCoordinatorService.handleWalletCreditedEvent(walletCreditedEvent);
    }

    @RabbitListener(queues = RabbitConstants.WALLET_DEBIT_FAILED_QUEUE)
    public void listenToWalletDebitFailedEvent(WalletDebitFailedEvent walletDebitFailedEvent) {
        logger.info("[WalletMessageListener] - Listening to WalletDebitFailedEvent: {}", walletDebitFailedEvent);
        paymentCoordinatorService.handleWalletDebitFailedEvent(walletDebitFailedEvent);
    }

    @RabbitListener(queues = RabbitConstants.WALLET_CREDIT_FAILED_QUEUE)
    public void listenToWalletCreditFailedEvent(WalletCreditFailedEvent walletCreditFailedEvent) {
        logger.info("[WalletMessageListener] - Listening to WalletCreditFailedEvent: {}", walletCreditFailedEvent);
        paymentCoordinatorService.handleWalletCreditFailedEvent(walletCreditFailedEvent);
    }

    @RabbitListener(queues = RabbitConstants.WALLET_REFUNDED_QUEUE)
    public void listenToWalletRefundedEvent(WalletRefundedEvent walletRefundedEvent) {
        logger.info("[WalletMessageListener] - Listening to WalletRefundedEvent: {}", walletRefundedEvent);
        paymentCoordinatorService.handleWalletRefundedEvent(walletRefundedEvent);
    }
}
