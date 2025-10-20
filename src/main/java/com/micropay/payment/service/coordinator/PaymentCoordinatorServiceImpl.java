package com.micropay.payment.service.coordinator;

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
import com.micropay.payment.mapper.NotificationEventMapper;
import com.micropay.payment.mapper.TransactionEventMapper;
import com.micropay.payment.mapper.WalletEventMapper;
import com.micropay.payment.service.messaging.notification.NotificationMessageDispatcher;
import com.micropay.payment.service.messaging.transaction.TransactionMessageDispatcher;
import com.micropay.payment.service.messaging.wallet.WalletMessageDispatcher;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.payment.PaymentStatus;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.payment.PaymentDataAccessService;
import com.micropay.payment.service.processor.refund.RefundProcessorService;
import com.micropay.payment.service.processor.withdrawal.WithdrawalProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCoordinatorServiceImpl implements PaymentCoordinatorService {

    private final WithdrawalProcessorService withdrawalProcessorService;
    private final RefundProcessorService refundProcessorService;
    private final PaymentDataAccessService paymentDataAccessService;

    private final TransactionMessageDispatcher transactionMessageDispatcher;
    private final WalletMessageDispatcher walletMessageDispatcher;
    private final NotificationMessageDispatcher notificationMessageDispatcher;

    private final NotificationEventMapper notificationEventMapper;
    private final TransactionEventMapper transactionEventMapper;
    private final WalletEventMapper walletEventMapper;

    @Override
    public void handleTransactionCreatedEvent(TransactionCreatedEvent transactionCreatedEvent) {
        PaymentModel payment = paymentDataAccessService.updatePaymentWithTransactionId(
                transactionCreatedEvent.paymentId(),
                transactionCreatedEvent.transactionId()
        );
        if (payment.getType().compareTo(TransactionType.TOP_UP) == 0) {
            CreditWalletEvent creditWalletEvent = walletEventMapper.mapToCreditWalletEvent(payment);

            walletMessageDispatcher.publishCreditWalletEvent(creditWalletEvent);
            return;
        }
        DebitWalletEvent debitWalletEvent = walletEventMapper.mapToDebitWalletEvent(payment);
        walletMessageDispatcher.publishDebitWalletEvent(debitWalletEvent);
    }

    @Override
    public void handleWalletDebitedEvent(WalletDebitedEvent walletDebitedEvent) {
        PaymentModel payment = paymentDataAccessService.updatePaymentStatus(
                walletDebitedEvent.paymentId(),
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

                RefundWalletEvent refundWalletEvent = walletEventMapper.mapToRefundWalletEvent(payment);
                walletMessageDispatcher.publishRefundWalletEvent(refundWalletEvent);
            }
            return;
        }
        CreditWalletEvent creditWalletEvent = walletEventMapper.mapToCreditWalletEvent(payment);
        walletMessageDispatcher.publishCreditWalletEvent(creditWalletEvent);
    }

    @Override
    public void handleWalletCreditedEvent(WalletCreditedEvent walletCreditedEvent) {
        PaymentModel payment = paymentDataAccessService
                .updatePaymentStatus(walletCreditedEvent.paymentId(), PaymentStatus.CREDITED, null);

        managePaymentFlowSuccess(payment, payment.getDestination(), NotificationConstants.WALLET_CREDITED_MESSAGE);

        paymentDataAccessService.updatePaymentStatus(payment.getId(), PaymentStatus.COMPLETED, null);
    }

    @Override
    public void handleWalletDebitFailedEvent(WalletDebitFailedEvent walletDebitFailedEvent) {
        PaymentModel payment = paymentDataAccessService.updatePaymentStatus(
                walletDebitFailedEvent.paymentId(),
                PaymentStatus.DEBIT_FAILED, walletDebitFailedEvent.failureReason());

        managePaymentFlowFailure(payment, walletDebitFailedEvent.failureReason(), payment.getSource());
    }

    @Override
    public void handleWalletCreditFailedEvent(WalletCreditFailedEvent walletCreditFailedEvent) {
        PaymentModel payment = paymentDataAccessService.updatePaymentStatus(
                walletCreditFailedEvent.paymentId(),
                PaymentStatus.CREDIT_FAILED, walletCreditFailedEvent.failureReason());

        if (payment.getType().compareTo(TransactionType.TOP_UP) == 0) {
            refundProcessorService.processRefund(payment);

            paymentDataAccessService
                    .updatePaymentStatus(payment.getId(), PaymentStatus.REFUNDED, null);

            managePaymentFlowFailure(payment, walletCreditFailedEvent.failureReason(), payment.getDestination());
            return;
        }
        managePaymentFlowFailure(payment, walletCreditFailedEvent.failureReason(), payment.getSource());

        RefundWalletEvent refundWalletEvent = walletEventMapper.mapToRefundWalletEvent(payment);
        walletMessageDispatcher.publishRefundWalletEvent(refundWalletEvent);
    }

    @Override
    public void handleWalletRefundedEvent(WalletRefundedEvent walletRefundedEvent) {
        paymentDataAccessService.updatePaymentStatus(
                walletRefundedEvent.paymentId(), PaymentStatus.REFUNDED, null);
    }

    private void managePaymentFlowFailure(PaymentModel payment, String failureReason, String subscriberId) {
        FailureNotificationEvent failureNotificationEvent = notificationEventMapper
                .mapToFailureNotificationEvent(payment, subscriberId);
        notificationMessageDispatcher.publishFailureNotification(failureNotificationEvent);

        TransactionFailedEvent transactionFailedEvent = transactionEventMapper
                .mapToTransactionFailedEvent(payment, failureReason);
        transactionMessageDispatcher.publishTransactionFailedEvent(transactionFailedEvent);
    }

    private void managePaymentFlowSuccess(PaymentModel payment, String subscriberId, String message) {
        SuccessNotificationEvent successNotificationEvent = notificationEventMapper
                .mapToSuccessNotificationEvent(payment, subscriberId, message);
        notificationMessageDispatcher.publishSuccessNotification(successNotificationEvent);

        TransactionSucceededEvent transactionSucceededEvent = transactionEventMapper
                .mapToTransactionSucceededEvent(payment);
        transactionMessageDispatcher.publishTransactionSucceededEvent(transactionSucceededEvent);
    }

}
