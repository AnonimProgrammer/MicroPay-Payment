package com.micropay.payment.service.coordinator;

import com.micropay.payment.dto.notification.FailureNotificationEvent;
import com.micropay.payment.dto.notification.SuccessNotificationEvent;
import com.micropay.payment.dto.transaction.TransactionCreatedEvent;
import com.micropay.payment.dto.transaction.TransactionFailedEvent;
import com.micropay.payment.dto.transaction.TransactionSucceededEvent;
import com.micropay.payment.dto.wallet.credit.CreditWalletEvent;
import com.micropay.payment.dto.wallet.debit.DebitWalletEvent;
import com.micropay.payment.dto.wallet.debit.WalletDebitedEvent;
import com.micropay.payment.dto.wallet.credit.WalletCreditedEvent;
import com.micropay.payment.dto.wallet.debit.WalletDebitFailedEvent;
import com.micropay.payment.dto.wallet.credit.WalletCreditFailedEvent;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;

class PaymentCoordinatorServiceImplTest {

    private PaymentCoordinatorServiceImpl coordinatorService;

    private WithdrawalProcessorService withdrawalProcessorService;
    private RefundProcessorService refundProcessorService;
    private PaymentDataAccessService paymentDataAccessService;

    private TransactionMessageDispatcher transactionMessageDispatcher;
    private WalletMessageDispatcher walletMessageDispatcher;
    private NotificationMessageDispatcher notificationMessageDispatcher;

    private NotificationEventMapper notificationEventMapper;
    private TransactionEventMapper transactionEventMapper;
    private WalletEventMapper walletEventMapper;

    private final static UUID eventId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        withdrawalProcessorService = mock(WithdrawalProcessorService.class);
        refundProcessorService = mock(RefundProcessorService.class);
        paymentDataAccessService = mock(PaymentDataAccessService.class);

        transactionMessageDispatcher = mock(TransactionMessageDispatcher.class);
        walletMessageDispatcher = mock(WalletMessageDispatcher.class);
        notificationMessageDispatcher = mock(NotificationMessageDispatcher.class);

        notificationEventMapper = mock(NotificationEventMapper.class);
        transactionEventMapper = mock(TransactionEventMapper.class);
        walletEventMapper = mock(WalletEventMapper.class);

        coordinatorService = new PaymentCoordinatorServiceImpl(
                withdrawalProcessorService,
                refundProcessorService,
                paymentDataAccessService,
                transactionMessageDispatcher,
                walletMessageDispatcher,
                notificationMessageDispatcher,
                notificationEventMapper,
                transactionEventMapper,
                walletEventMapper
        );
    }

    @Test
    void handleTransactionCreatedEvent_topUp_shouldPublishCreditWalletEvent() {
        TransactionCreatedEvent event = new TransactionCreatedEvent(eventId, 1L, UUID.randomUUID());
        PaymentModel payment = mock(PaymentModel.class);

        when(payment.getType()).thenReturn(TransactionType.TOP_UP);
        when(paymentDataAccessService
                .updatePaymentWithTransactionId(event.paymentId(), event.transactionId())).thenReturn(payment);

        CreditWalletEvent creditEvent = mock(CreditWalletEvent.class);
        when(walletEventMapper.mapToCreditWalletEvent(payment)).thenReturn(creditEvent);

        coordinatorService.handleTransactionCreatedEvent(event);

        verify(walletMessageDispatcher).publishCreditWalletEvent(creditEvent);
        verify(walletMessageDispatcher, never()).publishDebitWalletEvent(any());
    }

    @Test
    void handleTransactionCreatedEvent_shouldPublishDebitWalletEvent() {
        TransactionCreatedEvent event = new TransactionCreatedEvent(eventId, 1L, UUID.randomUUID());
        PaymentModel payment = mock(PaymentModel.class);

        when(payment.getType()).thenReturn(TransactionType.WITHDRAWAL);
        when(paymentDataAccessService
                .updatePaymentWithTransactionId(event.paymentId(), event.transactionId())).thenReturn(payment);

        DebitWalletEvent debitEvent = mock(DebitWalletEvent.class);
        when(walletEventMapper.mapToDebitWalletEvent(payment)).thenReturn(debitEvent);

        coordinatorService.handleTransactionCreatedEvent(event);

        verify(walletMessageDispatcher).publishDebitWalletEvent(debitEvent);
        verify(walletMessageDispatcher, never()).publishCreditWalletEvent(any());
    }

    @Test
    void handleWalletDebitedEvent_withdrawal_successPath_shouldProcessWithdrawalAndUpdateStatuses() {
        WalletDebitedEvent event = mock(WalletDebitedEvent.class);
        PaymentModel payment = mock(PaymentModel.class);

        when(payment.getType()).thenReturn(TransactionType.WITHDRAWAL);
        when(payment.getId()).thenReturn(1L);
        when(paymentDataAccessService
                .updatePaymentStatus(anyLong(), eq(PaymentStatus.DEBITED), isNull())).thenReturn(payment);

        coordinatorService.handleWalletDebitedEvent(event);

        verify(withdrawalProcessorService).processWithdrawal(payment);
        verify(paymentDataAccessService, times(1))
                .updatePaymentStatus(payment.getId(), PaymentStatus.CREDITED, null);
        verify(paymentDataAccessService, times(1))
                .updatePaymentStatus(payment.getId(), PaymentStatus.COMPLETED, null);
    }

    @Test
    void handleWalletDebitedEvent_shouldPublishCreditWalletEvent() {
        WalletDebitedEvent event = mock(WalletDebitedEvent.class);
        PaymentModel payment = new PaymentModel();
        payment.setId(1L);
        payment.setType(TransactionType.TRANSFER);
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setSource("111");
        payment.setDestination("222");

        when(paymentDataAccessService
                .updatePaymentStatus(anyLong(), eq(PaymentStatus.DEBITED), isNull()))
                .thenReturn(payment);

        CreditWalletEvent creditWalletEvent = mock(CreditWalletEvent.class);
        when(walletEventMapper.mapToCreditWalletEvent(payment)).thenReturn(creditWalletEvent);

        coordinatorService.handleWalletDebitedEvent(event);

        verify(walletEventMapper).mapToCreditWalletEvent(payment);
        verify(walletMessageDispatcher).publishCreditWalletEvent(creditWalletEvent);
    }

    @Test
    void handleWalletCreditedEvent_shouldUpdatePaymentAndSendSuccessNotification() {
        WalletCreditedEvent event = mock(WalletCreditedEvent.class);
        PaymentModel payment = new PaymentModel();
        payment.setId(1L);
        payment.setDestination("222");

        SuccessNotificationEvent successEvent = mock(SuccessNotificationEvent.class);
        TransactionSucceededEvent transactionSucceededEvent = mock(TransactionSucceededEvent.class);

        when(paymentDataAccessService
                .updatePaymentStatus(anyLong(), eq(PaymentStatus.CREDITED), isNull())).thenReturn(payment);

        when(notificationEventMapper
                .mapToSuccessNotificationEvent(any(PaymentModel.class), anyString(), anyString())).thenReturn(successEvent);

        when(transactionEventMapper
                .mapToTransactionSucceededEvent(any(PaymentModel.class))).thenReturn(transactionSucceededEvent);

        coordinatorService.handleWalletCreditedEvent(event);

        verify(paymentDataAccessService).updatePaymentStatus(payment.getId(), PaymentStatus.COMPLETED, null);
        verify(notificationMessageDispatcher).publishSuccessNotification(successEvent);
        verify(transactionMessageDispatcher).publishTransactionSucceededEvent(transactionSucceededEvent);
    }

    @Test
    void handleWalletDebitFailedEvent_shouldUpdatePaymentAndSendFailureNotification() {
        WalletDebitFailedEvent event = mock(WalletDebitFailedEvent.class);
        when(event.paymentId()).thenReturn(1L);
        when(event.failureReason()).thenReturn("fail");

        PaymentModel payment = new PaymentModel();
        payment.setId(1L);
        payment.setSource("222");
        when(paymentDataAccessService
                .updatePaymentStatus(1L, PaymentStatus.DEBIT_FAILED, "fail")).thenReturn(payment);

        FailureNotificationEvent failureNotificationEvent = mock(FailureNotificationEvent.class);
        TransactionFailedEvent transactionFailedEvent = mock(TransactionFailedEvent.class);

        when(notificationEventMapper.mapToFailureNotificationEvent(any(), anyString()))
                .thenReturn(failureNotificationEvent);
        when(transactionEventMapper.mapToTransactionFailedEvent(any(), anyString()))
                .thenReturn(transactionFailedEvent);

        coordinatorService.handleWalletDebitFailedEvent(event);

        verify(notificationMessageDispatcher).publishFailureNotification(failureNotificationEvent);
        verify(transactionMessageDispatcher).publishTransactionFailedEvent(transactionFailedEvent);
    }

    @Test
    void handleWalletCreditFailedEvent_topUp_shouldProcessRefund() {
        WalletCreditFailedEvent event = mock(WalletCreditFailedEvent.class);
        when(event.paymentId()).thenReturn(1L);
        when(event.failureReason()).thenReturn("fail");

        PaymentModel payment = new PaymentModel();
        payment.setId(1L);
        payment.setDestination("222");
        payment.setType(TransactionType.TOP_UP);

        when(paymentDataAccessService
                .updatePaymentStatus(1L, PaymentStatus.CREDIT_FAILED, "fail")).thenReturn(payment);

        FailureNotificationEvent failureNotificationEvent = mock(FailureNotificationEvent.class);
        when(notificationEventMapper
                .mapToFailureNotificationEvent(any(), anyString())).thenReturn(failureNotificationEvent);

        coordinatorService.handleWalletCreditFailedEvent(event);

        verify(refundProcessorService).processRefund(payment);
        verify(paymentDataAccessService).updatePaymentStatus(payment.getId(), PaymentStatus.REFUNDED, null);
        verify(notificationMessageDispatcher).publishFailureNotification(failureNotificationEvent);
    }

    @Test
    void handleWalletCreditFailedEvent_shouldProcessRefundWalletEvent() {
        WalletCreditFailedEvent event = mock(WalletCreditFailedEvent.class);

        when(event.paymentId()).thenReturn(1L);
        when(event.failureReason()).thenReturn("fail");

        PaymentModel payment = mock(PaymentModel.class);
        when(payment.getType()).thenReturn(TransactionType.TRANSFER);
        when(payment.getId()).thenReturn(1L);

        when(paymentDataAccessService
                .updatePaymentStatus(1L, PaymentStatus.CREDIT_FAILED, "fail")).thenReturn(payment);

        RefundWalletEvent refundWalletEvent = mock(RefundWalletEvent.class);
        when(walletEventMapper.mapToRefundWalletEvent(payment)).thenReturn(refundWalletEvent);

        coordinatorService.handleWalletCreditFailedEvent(event);

        verify(walletEventMapper).mapToRefundWalletEvent(payment);
        verify(walletMessageDispatcher).publishRefundWalletEvent(refundWalletEvent);
    }

    @Test
    void handleWalletRefundedEvent_shouldUpdatePaymentStatus() {
        WalletRefundedEvent event = mock(WalletRefundedEvent.class);

        coordinatorService.handleWalletRefundedEvent(event);

        verify(paymentDataAccessService)
                .updatePaymentStatus(event.paymentId(), PaymentStatus.REFUNDED, null);
    }
}
