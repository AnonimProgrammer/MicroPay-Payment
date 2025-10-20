package com.micropay.payment.service.messaging.wallet;

import com.micropay.payment.dto.wallet.credit.WalletCreditFailedEvent;
import com.micropay.payment.dto.wallet.credit.WalletCreditedEvent;
import com.micropay.payment.dto.wallet.debit.WalletDebitFailedEvent;
import com.micropay.payment.dto.wallet.debit.WalletDebitedEvent;
import com.micropay.payment.dto.wallet.refund.WalletRefundedEvent;
import com.micropay.payment.service.coordinator.PaymentCoordinatorService;
import com.micropay.payment.service.messaging.inbox.InboxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;

class WalletMessageListenerTest {

    @Mock
    private PaymentCoordinatorService paymentCoordinatorService;

    @Mock
    private InboxService inboxService;

    @InjectMocks
    private WalletMessageListener listener;

    private final static UUID EVENT_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldHandleWalletDebitedEventSuccessfully() {
        WalletDebitedEvent event = new WalletDebitedEvent(EVENT_ID, 1L, 1L, BigDecimal.valueOf(100));

        doNothing().when(inboxService).checkInbox(EVENT_ID);

        listener.listenToWalletDebitedEvent(event);

        verify(inboxService).checkInbox(EVENT_ID);
        verify(paymentCoordinatorService).handleWalletDebitedEvent(event);
    }

    @Test
    void shouldNotCallCoordinatorWhenWalletDebitedInboxThrows() {
        WalletDebitedEvent event = new WalletDebitedEvent(EVENT_ID, 1L, 1L, BigDecimal.valueOf(100));

        doThrow(new RuntimeException()).when(inboxService).checkInbox(EVENT_ID);

        listener.listenToWalletDebitedEvent(event);

        verify(inboxService).checkInbox(EVENT_ID);
        verify(paymentCoordinatorService, never()).handleWalletDebitedEvent(any());
    }

    @Test
    void shouldHandleWalletCreditedEventSuccessfully() {
        WalletCreditedEvent event = new WalletCreditedEvent(EVENT_ID, 1L, 1L, BigDecimal.valueOf(100));

        doNothing().when(inboxService).checkInbox(EVENT_ID);

        listener.listenToWalletCreditedEvent(event);

        verify(inboxService).checkInbox(EVENT_ID);
        verify(paymentCoordinatorService).handleWalletCreditedEvent(event);
    }

    @Test
    void shouldNotCallCoordinatorWhenWalletCreditedInboxThrows() {
        WalletCreditedEvent event = new WalletCreditedEvent(EVENT_ID, 1L, 1L, BigDecimal.valueOf(100));

        doThrow(new RuntimeException()).when(inboxService).checkInbox(EVENT_ID);

        listener.listenToWalletCreditedEvent(event);

        verify(inboxService).checkInbox(EVENT_ID);
        verify(paymentCoordinatorService, never()).handleWalletCreditedEvent(any());
    }

    @Test
    void shouldHandleWalletDebitFailedEventSuccessfully() {
        WalletDebitFailedEvent event = new WalletDebitFailedEvent(EVENT_ID, 1L, 1L, "Error.");

        doNothing().when(inboxService).checkInbox(EVENT_ID);

        listener.listenToWalletDebitFailedEvent(event);

        verify(inboxService).checkInbox(EVENT_ID);
        verify(paymentCoordinatorService).handleWalletDebitFailedEvent(event);
    }

    @Test
    void shouldNotCallCoordinatorWhenWalletDebitFailedInboxThrows() {
        WalletDebitFailedEvent event = new WalletDebitFailedEvent(EVENT_ID, 1L, 1L, "Error.");

        doThrow(new RuntimeException()).when(inboxService).checkInbox(EVENT_ID);

        listener.listenToWalletDebitFailedEvent(event);

        verify(inboxService).checkInbox(EVENT_ID);
        verify(paymentCoordinatorService, never()).handleWalletDebitFailedEvent(any());
    }

    @Test
    void shouldHandleWalletCreditFailedEventSuccessfully() {
        WalletCreditFailedEvent event = new WalletCreditFailedEvent(EVENT_ID, 1L, 1L, "Error.");

        doNothing().when(inboxService).checkInbox(EVENT_ID);

        listener.listenToWalletCreditFailedEvent(event);

        verify(inboxService).checkInbox(EVENT_ID);
        verify(paymentCoordinatorService).handleWalletCreditFailedEvent(event);
    }

    @Test
    void shouldNotCallCoordinatorWhenWalletCreditFailedInboxThrows() {
        WalletCreditFailedEvent event = new WalletCreditFailedEvent(EVENT_ID, 1L, 1L, "Error.");

        doThrow(new RuntimeException()).when(inboxService).checkInbox(EVENT_ID);

        listener.listenToWalletCreditFailedEvent(event);

        verify(inboxService).checkInbox(EVENT_ID);
        verify(paymentCoordinatorService, never()).handleWalletCreditFailedEvent(any());
    }

    @Test
    void shouldHandleWalletRefundedEventSuccessfully() {
        WalletRefundedEvent event = new WalletRefundedEvent(EVENT_ID, 1L, 1L, BigDecimal.valueOf(100));

        doNothing().when(inboxService).checkInbox(EVENT_ID);

        listener.listenToWalletRefundedEvent(event);

        verify(inboxService).checkInbox(EVENT_ID);
        verify(paymentCoordinatorService).handleWalletRefundedEvent(event);
    }

    @Test
    void shouldNotCallCoordinatorWhenWalletRefundedInboxThrows() {
        WalletRefundedEvent event = new WalletRefundedEvent(EVENT_ID, 1L, 1L, BigDecimal.valueOf(100));

        doThrow(new RuntimeException()).when(inboxService).checkInbox(EVENT_ID);

        listener.listenToWalletRefundedEvent(event);

        verify(inboxService).checkInbox(EVENT_ID);
        verify(paymentCoordinatorService, never()).handleWalletRefundedEvent(any());
    }
}
