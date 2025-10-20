package com.micropay.payment.service.messaging.transaction;

import com.micropay.payment.dto.transaction.TransactionCreatedEvent;
import com.micropay.payment.service.coordinator.PaymentCoordinatorService;
import com.micropay.payment.service.messaging.inbox.InboxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.UUID;

import static org.mockito.Mockito.*;

class TransactionMessageListenerTest {

    @Mock
    private PaymentCoordinatorService paymentCoordinatorService;

    @Mock
    private InboxService inboxService;

    @InjectMocks
    private TransactionMessageListener listener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldHandleTransactionCreatedEventSuccessfully() {
        UUID eventId = UUID.randomUUID();
        TransactionCreatedEvent event = new TransactionCreatedEvent(eventId, 1L, UUID.randomUUID());

        doNothing().when(inboxService).checkInbox(eventId);

        listener.listenToTransactionCreatedEvent(event);

        verify(inboxService).checkInbox(eventId);
        verify(paymentCoordinatorService).handleTransactionCreatedEvent(event);
    }

    @Test
    void shouldNotCallCoordinatorWhenInboxThrowsException() {
        UUID eventId = UUID.randomUUID();
        TransactionCreatedEvent event = new TransactionCreatedEvent(eventId, 1L, UUID.randomUUID());

        doThrow(new RuntimeException("Inbox already contains such event."))
                .when(inboxService).checkInbox(eventId);

        listener.listenToTransactionCreatedEvent(event);

        verify(inboxService).checkInbox(eventId);
        verify(paymentCoordinatorService, never()).handleTransactionCreatedEvent(any());
    }
}
