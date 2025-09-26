package com.micropay.payment.service;

import com.micropay.payment.config.NotificationConstants;
import com.micropay.payment.dto.notification.FailureNotificationEvent;
import com.micropay.payment.dto.notification.SuccessNotificationEvent;
import com.micropay.payment.dto.transaction.TransactionCreatedEvent;
import com.micropay.payment.dto.transaction.TransactionFailedEvent;
import com.micropay.payment.dto.transaction.TransactionSucceededEvent;
import com.micropay.payment.dto.wallet.credit.CreditWalletEvent;
import com.micropay.payment.dto.wallet.credit.WalletCreditFailedEvent;
import com.micropay.payment.dto.wallet.credit.WalletCreditedEvent;
import com.micropay.payment.dto.wallet.debit.DebitWalletEvent;
import com.micropay.payment.dto.wallet.debit.WalletDebitFailedEvent;
import com.micropay.payment.dto.wallet.debit.WalletDebitedEvent;
import com.micropay.payment.dto.wallet.refund.RefundWalletEvent;
import com.micropay.payment.dto.wallet.refund.WalletRefundedEvent;
import com.micropay.payment.factory.NotificationEventFactory;
import com.micropay.payment.factory.TransactionEventFactory;
import com.micropay.payment.factory.WalletEventFactory;
import com.micropay.payment.messaging.notification.NotificationMessagePublisher;
import com.micropay.payment.messaging.transaction.TransactionMessagePublisher;
import com.micropay.payment.messaging.wallet.WalletMessagePublisher;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.payment.PaymentStatus;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.processor.refund.RefundProcessorService;
import com.micropay.payment.service.processor.withdrawal.WithdrawalProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCoordinatorService {

    private final WithdrawalProcessorService withdrawalProcessorService;
    private final RefundProcessorService refundProcessorService;
    private final PaymentDataAccessService paymentDataAccessService;

    private final TransactionMessagePublisher transactionMessagePublisher;
    private final WalletMessagePublisher walletMessagePublisher;
    private final NotificationMessagePublisher notificationMessagePublisher;

    private final NotificationEventFactory notificationEventFactory;
    private final TransactionEventFactory transactionEventFactory;
    private final WalletEventFactory walletEventFactory;

    public void handleTransactionCreatedEvent(TransactionCreatedEvent transactionCreatedEvent) {
        PaymentModel payment = paymentDataAccessService.updatePaymentWithTransactionId(
                transactionCreatedEvent.getPaymentId(),
                transactionCreatedEvent.getTransactionId()
        );
        if (payment.getType().compareTo(TransactionType.TOP_UP) == 0) {
            CreditWalletEvent creditWalletEvent = walletEventFactory
                    .createCreditWalletEvent(payment);
            walletMessagePublisher.publishCreditWalletEvent(creditWalletEvent);
            return;
        }
        DebitWalletEvent debitWalletEvent = walletEventFactory.createDebitWalletEvent(payment);
        walletMessagePublisher.publishDebitWalletEvent(debitWalletEvent);
    }

    public void handleWalletDebitedEvent(WalletDebitedEvent walletDebitedEvent) {
        PaymentModel payment = paymentDataAccessService.updatePaymentStatus(
                walletDebitedEvent.getPaymentId(),
                PaymentStatus.DEBITED, null
        );
        if (payment.getType().compareTo(TransactionType.WITHDRAWAL) == 0) {
            try {
                withdrawalProcessorService.processWithdrawal(payment);
                paymentDataAccessService
                        .updatePaymentStatus(payment.getId(), PaymentStatus.CREDITED, null);

                managePaymentFlowSuccess(payment, payment.getSource(), NotificationConstants.WALLET_DEBITED_MESSAGE);

                paymentDataAccessService
                        .updatePaymentStatus(payment.getId(), PaymentStatus.COMPLETED, null);
            } catch (Exception exception) {
                paymentDataAccessService
                        .updatePaymentStatus(payment.getId(), PaymentStatus.CREDIT_FAILED, exception.getMessage());

                managePaymentFlowFailure(payment, exception.getMessage(), payment.getSource());

                RefundWalletEvent refundWalletEvent = walletEventFactory.createRefundWalletEvent(payment);
                walletMessagePublisher.publishRefundWalletEvent(refundWalletEvent);
            }
            return;
        }
        CreditWalletEvent creditWalletEvent = walletEventFactory.createCreditWalletEvent(payment);
        walletMessagePublisher.publishCreditWalletEvent(creditWalletEvent);
    }

    public void handleWalletCreditedEvent(WalletCreditedEvent walletCreditedEvent) {
        PaymentModel payment = paymentDataAccessService
                .updatePaymentStatus(walletCreditedEvent.getPaymentId(), PaymentStatus.CREDITED, null);

        managePaymentFlowSuccess(payment, payment.getDestination(), NotificationConstants.WALLET_CREDITED_MESSAGE);

        paymentDataAccessService.updatePaymentStatus(payment.getId(), PaymentStatus.COMPLETED, null);
    }

    public void handleWalletDebitFailedEvent(WalletDebitFailedEvent walletDebitFailedEvent) {
        PaymentModel payment = paymentDataAccessService.updatePaymentStatus(
                walletDebitFailedEvent.getPaymentId(),
                PaymentStatus.DEBIT_FAILED, walletDebitFailedEvent.getFailureReason());

        managePaymentFlowFailure(payment, walletDebitFailedEvent.getFailureReason(), payment.getSource());
    }

    public void handleWalletCreditFailedEvent(WalletCreditFailedEvent walletCreditFailedEvent) {
        PaymentModel payment = paymentDataAccessService.updatePaymentStatus(
                walletCreditFailedEvent.getPaymentId(),
                PaymentStatus.CREDIT_FAILED, walletCreditFailedEvent.getFailureReason());

        if (payment.getType().compareTo(TransactionType.TOP_UP) == 0) {
            refundProcessorService.processRefund(payment);

            paymentDataAccessService
                    .updatePaymentStatus(payment.getId(), PaymentStatus.REFUNDED, null);

            managePaymentFlowFailure(payment, walletCreditFailedEvent.getFailureReason(), payment.getDestination());
            return;
        }

        managePaymentFlowFailure(payment, walletCreditFailedEvent.getFailureReason(), payment.getSource());

        RefundWalletEvent refundWalletEvent = walletEventFactory.createRefundWalletEvent(payment);
        walletMessagePublisher.publishRefundWalletEvent(refundWalletEvent);
    }

    public void handleWalletRefundedEvent(WalletRefundedEvent walletRefundedEvent) {
        paymentDataAccessService.updatePaymentStatus(
                walletRefundedEvent.getPaymentId(), PaymentStatus.REFUNDED, null);
    }

    private void managePaymentFlowFailure(PaymentModel payment, String failureReason, String subscriberId) {
        FailureNotificationEvent failureNotificationEvent = notificationEventFactory
                .createFailureNotificationEvent(payment, subscriberId);
        notificationMessagePublisher.publishFailureNotification(failureNotificationEvent);

        TransactionFailedEvent transactionFailedEvent = transactionEventFactory
                .createTransactionFailedEvent(payment, failureReason);
        transactionMessagePublisher.publishTransactionFailedEvent(transactionFailedEvent);
    }

    private void managePaymentFlowSuccess(PaymentModel payment, String subscriberId, String message) {
        SuccessNotificationEvent successNotificationEvent = notificationEventFactory
                .createSuccessNotificationEvent(payment, subscriberId, message);
        notificationMessagePublisher.publishSuccessNotification(successNotificationEvent);

        TransactionSucceededEvent transactionSucceededEvent = transactionEventFactory
                .createTransactionSucceededEvent(payment);
        transactionMessagePublisher.publishTransactionSucceededEvent(transactionSucceededEvent);
    }

}
