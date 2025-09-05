package com.payment.service;

import com.payment.dto.notification.FailureNotificationEvent;
import com.payment.dto.notification.SuccessNotificationEvent;
import com.payment.dto.transaction.TransactionCreatedEvent;
import com.payment.dto.transaction.TransactionFailedEvent;
import com.payment.dto.transaction.TransactionSucceededEvent;
import com.payment.dto.wallet.credit.CreditWalletEvent;
import com.payment.dto.wallet.credit.WalletCreditFailedEvent;
import com.payment.dto.wallet.credit.WalletCreditedEvent;
import com.payment.dto.wallet.debit.DebitWalletEvent;
import com.payment.dto.wallet.debit.WalletDebitFailedEvent;
import com.payment.dto.wallet.debit.WalletDebitedEvent;
import com.payment.dto.wallet.refund.RefundWalletEvent;
import com.payment.dto.wallet.refund.WalletRefundedEvent;
import com.payment.factory.NotificationEventFactory;
import com.payment.factory.TransactionEventFactory;
import com.payment.factory.WalletEventFactory;
import com.payment.messaging.notification.NotificationMessagePublisher;
import com.payment.messaging.transaction.TransactionMessagePublisher;
import com.payment.messaging.wallet.WalletMessagePublisher;
import com.payment.model.payment.PaymentModel;
import com.payment.model.payment.PaymentStatus;
import com.payment.model.transaction.TransactionType;
import com.payment.service.processor.refund.RefundProcessorService;
import com.payment.service.processor.withdrawal.WithdrawalProcessorService;
import org.springframework.stereotype.Service;

@Service
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

    public PaymentCoordinatorService(
            WithdrawalProcessorService withdrawalProcessorService, RefundProcessorService refundProcessorService,
            PaymentDataAccessService paymentDataAccessService, TransactionMessagePublisher transactionMessagePublisher,
            WalletMessagePublisher walletMessagePublisher, NotificationMessagePublisher notificationMessagePublisher,
            NotificationEventFactory notificationEventFactory, TransactionEventFactory transactionEventFactory,
            WalletEventFactory walletEventFactory
    ) {
        this.withdrawalProcessorService = withdrawalProcessorService;
        this.refundProcessorService = refundProcessorService;
        this.paymentDataAccessService = paymentDataAccessService;
        this.transactionMessagePublisher = transactionMessagePublisher;
        this.walletMessagePublisher = walletMessagePublisher;
        this.notificationMessagePublisher = notificationMessagePublisher;
        this.notificationEventFactory = notificationEventFactory;
        this.transactionEventFactory = transactionEventFactory;
        this.walletEventFactory = walletEventFactory;
    }

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

                TransactionSucceededEvent transactionSucceededEvent = transactionEventFactory
                        .createTransactionSucceededEvent(payment);
                transactionMessagePublisher.publishTransactionSucceededEvent(transactionSucceededEvent);

                paymentDataAccessService
                        .updatePaymentStatus(payment.getId(), PaymentStatus.COMPLETED, null);
            } catch (Exception exception) {
                paymentDataAccessService
                        .updatePaymentStatus(payment.getId(), PaymentStatus.CREDIT_FAILED, exception.getMessage());

                FailureNotificationEvent failureNotificationEvent = notificationEventFactory
                        .createFailureNotificationEvent(payment, walletDebitedEvent.getUserId());
                notificationMessagePublisher.publishFailureNotification(failureNotificationEvent);

                TransactionFailedEvent transactionFailedEvent = transactionEventFactory
                        .createTransactionFailedEvent(payment, exception.getMessage());
                transactionMessagePublisher.publishTransactionFailedEvent(transactionFailedEvent);

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

        TransactionSucceededEvent transactionSucceededEvent = transactionEventFactory
                .createTransactionSucceededEvent(payment);
        transactionMessagePublisher.publishTransactionSucceededEvent(transactionSucceededEvent);

        SuccessNotificationEvent successNotificationEvent = notificationEventFactory
                .createSuccessNotificationEvent(payment, walletCreditedEvent.getUserId());
        notificationMessagePublisher.publishSuccessNotification(successNotificationEvent);

        paymentDataAccessService.updatePaymentStatus(payment.getId(), PaymentStatus.COMPLETED, null);
    }

    public void handleWalletDebitFailedEvent(WalletDebitFailedEvent walletDebitFailedEvent) {
        PaymentModel payment = paymentDataAccessService.updatePaymentStatus(
                walletDebitFailedEvent.getPaymentId(),
                PaymentStatus.DEBIT_FAILED, walletDebitFailedEvent.getFailureReason());

        FailureNotificationEvent failureNotificationEvent = notificationEventFactory.createFailureNotificationEvent(
                payment, walletDebitFailedEvent.getUserId());
        notificationMessagePublisher.publishFailureNotification(failureNotificationEvent);

        TransactionFailedEvent transactionFailedEvent = transactionEventFactory.createTransactionFailedEvent(
                payment, walletDebitFailedEvent.getFailureReason());
        transactionMessagePublisher.publishTransactionFailedEvent(transactionFailedEvent);
    }

    public void handleWalletCreditFailedEvent(WalletCreditFailedEvent walletCreditFailedEvent) {
        PaymentModel payment = paymentDataAccessService.updatePaymentStatus(
                walletCreditFailedEvent.getPaymentId(),
                PaymentStatus.CREDIT_FAILED, walletCreditFailedEvent.getFailureReason());

        TransactionFailedEvent transactionFailedEvent = transactionEventFactory.createTransactionFailedEvent(
                payment, walletCreditFailedEvent.getFailureReason());
        transactionMessagePublisher.publishTransactionFailedEvent(transactionFailedEvent);

        if (payment.getType().compareTo(TransactionType.TOP_UP) == 0) {
            refundProcessorService.processRefund(payment);

            paymentDataAccessService
                    .updatePaymentStatus(payment.getId(), PaymentStatus.REFUNDED, null);
            return;
        }
        FailureNotificationEvent failureNotificationEvent = notificationEventFactory.createFailureNotificationEvent(
                payment,  null);
        notificationMessagePublisher.publishFailureNotification(failureNotificationEvent);

        RefundWalletEvent refundWalletEvent = walletEventFactory.createRefundWalletEvent(payment);
        walletMessagePublisher.publishRefundWalletEvent(refundWalletEvent);
    }

    public void handleWalletRefundedEvent(WalletRefundedEvent walletRefundedEvent) {
        paymentDataAccessService.updatePaymentStatus(
                walletRefundedEvent.getPaymentId(), PaymentStatus.REFUNDED, null);
    }

}
