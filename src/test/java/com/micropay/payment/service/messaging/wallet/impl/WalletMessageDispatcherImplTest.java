package com.micropay.payment.service.messaging.wallet.impl;

import com.micropay.payment.config.RabbitConstants;
import com.micropay.payment.dto.wallet.credit.CreditWalletEvent;
import com.micropay.payment.dto.wallet.debit.DebitWalletEvent;
import com.micropay.payment.dto.wallet.refund.RefundWalletEvent;
import com.micropay.payment.model.event.entity.BaseEvent;
import com.micropay.payment.service.messaging.outbox.OutboxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

class WalletMessageDispatcherImplTest {

    @Mock
    private OutboxService outboxService;

    @InjectMocks
    private WalletMessageDispatcherImpl dispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldPublishDebitWalletEvent() {
        DebitWalletEvent event = mock(DebitWalletEvent.class);
        BaseEvent baseEvent = new BaseEvent();
        when(outboxService.saveBaseEvent(
                eq("DEBIT_WALLET"),
                eq(RabbitConstants.WALLET_DEBIT_EXCHANGE),
                eq(RabbitConstants.WALLET_DEBIT_ROUTING_KEY),
                eq(event)
        )).thenReturn(baseEvent);

        dispatcher.publishDebitWalletEvent(event);

        verify(outboxService).saveBaseEvent(
                "DEBIT_WALLET",
                RabbitConstants.WALLET_DEBIT_EXCHANGE,
                RabbitConstants.WALLET_DEBIT_ROUTING_KEY,
                event
        );
        verify(outboxService).publish(baseEvent);
    }

    @Test
    void shouldPublishCreditWalletEvent() {
        CreditWalletEvent event = mock(CreditWalletEvent.class);
        BaseEvent baseEvent = new BaseEvent();
        when(outboxService.saveBaseEvent(
                eq("CREDIT_WALLET"),
                eq(RabbitConstants.WALLET_CREDIT_EXCHANGE),
                eq(RabbitConstants.WALLET_CREDIT_ROUTING_KEY),
                eq(event)
        )).thenReturn(baseEvent);

        dispatcher.publishCreditWalletEvent(event);

        verify(outboxService).saveBaseEvent(
                "CREDIT_WALLET",
                RabbitConstants.WALLET_CREDIT_EXCHANGE,
                RabbitConstants.WALLET_CREDIT_ROUTING_KEY,
                event
        );
        verify(outboxService).publish(baseEvent);
    }

    @Test
    void shouldPublishRefundWalletEvent() {
        RefundWalletEvent event = mock(RefundWalletEvent.class);
        BaseEvent baseEvent = new BaseEvent();
        when(outboxService.saveBaseEvent(
                eq("REFUND_WALLET"),
                eq(RabbitConstants.WALLET_REFUND_EXCHANGE),
                eq(RabbitConstants.WALLET_REFUND_ROUTING_KEY),
                eq(event)
        )).thenReturn(baseEvent);

        dispatcher.publishRefundWalletEvent(event);

        verify(outboxService).saveBaseEvent(
                "REFUND_WALLET",
                RabbitConstants.WALLET_REFUND_EXCHANGE,
                RabbitConstants.WALLET_REFUND_ROUTING_KEY,
                event
        );
        verify(outboxService).publish(baseEvent);
    }
}
