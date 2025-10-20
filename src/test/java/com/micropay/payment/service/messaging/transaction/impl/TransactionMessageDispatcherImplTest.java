package com.micropay.payment.service.messaging.transaction.impl;

import com.micropay.payment.config.RabbitConstants;
import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.dto.transaction.TransactionFailedEvent;
import com.micropay.payment.dto.transaction.TransactionSucceededEvent;
import com.micropay.payment.model.event.entity.BaseEvent;
import com.micropay.payment.service.messaging.outbox.OutboxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

class TransactionMessageDispatcherImplTest {

    @Mock
    private OutboxService outboxService;

    @InjectMocks
    private TransactionMessageDispatcherImpl dispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldPublishInitiateTransactionEvent() {
        InitiateTransactionEvent event = mock(InitiateTransactionEvent.class);
        BaseEvent baseEvent = new BaseEvent();
        when(outboxService.saveBaseEvent(
                eq("INITIATE_TRANSACTION"),
                eq(RabbitConstants.TRANSACTION_EXCHANGE),
                eq(RabbitConstants.TRANSACTION_INITIATE_ROUTING_KEY),
                eq(event)
        )).thenReturn(baseEvent);

        dispatcher.publishInitiateTransactionEvent(event);

        verify(outboxService).saveBaseEvent(
                "INITIATE_TRANSACTION",
                RabbitConstants.TRANSACTION_EXCHANGE,
                RabbitConstants.TRANSACTION_INITIATE_ROUTING_KEY,
                event
        );
        verify(outboxService).publish(baseEvent);
    }

    @Test
    void shouldPublishTransactionSucceededEvent() {
        TransactionSucceededEvent event = mock(TransactionSucceededEvent.class);
        BaseEvent baseEvent = new BaseEvent();
        when(outboxService.saveBaseEvent(
                eq("TRANSACTION_SUCCEEDED"),
                eq(RabbitConstants.TRANSACTION_EXCHANGE),
                eq(RabbitConstants.TRANSACTION_SUCCEEDED_ROUTING_KEY),
                eq(event)
        )).thenReturn(baseEvent);

        dispatcher.publishTransactionSucceededEvent(event);

        verify(outboxService).saveBaseEvent(
                "TRANSACTION_SUCCEEDED",
                RabbitConstants.TRANSACTION_EXCHANGE,
                RabbitConstants.TRANSACTION_SUCCEEDED_ROUTING_KEY,
                event
        );
        verify(outboxService).publish(baseEvent);
    }

    @Test
    void shouldPublishTransactionFailedEvent() {
        TransactionFailedEvent event = mock(TransactionFailedEvent.class);
        BaseEvent baseEvent = new BaseEvent();
        when(outboxService.saveBaseEvent(
                eq("TRANSACTION_FAILED"),
                eq(RabbitConstants.TRANSACTION_EXCHANGE),
                eq(RabbitConstants.TRANSACTION_FAILED_ROUTING_KEY),
                eq(event)
        )).thenReturn(baseEvent);

        dispatcher.publishTransactionFailedEvent(event);

        verify(outboxService).saveBaseEvent(
                "TRANSACTION_FAILED",
                RabbitConstants.TRANSACTION_EXCHANGE,
                RabbitConstants.TRANSACTION_FAILED_ROUTING_KEY,
                event
        );
        verify(outboxService).publish(baseEvent);
    }
}
