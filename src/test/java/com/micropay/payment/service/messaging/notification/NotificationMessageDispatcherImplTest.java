package com.micropay.payment.service.messaging.notification;

import com.micropay.payment.config.RabbitConstants;
import com.micropay.payment.dto.notification.FailureNotificationEvent;
import com.micropay.payment.dto.notification.SuccessNotificationEvent;
import com.micropay.payment.model.event.entity.BaseEvent;
import com.micropay.payment.service.messaging.outbox.OutboxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

class NotificationMessageDispatcherImplTest {

    @Mock
    private OutboxService outboxService;

    @InjectMocks
    private NotificationMessageDispatcherImpl dispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldPublishSuccessNotification() {
        SuccessNotificationEvent event = mock(SuccessNotificationEvent.class);
        BaseEvent baseEvent = new BaseEvent();

        when(outboxService.saveBaseEvent(
                eq("SUCCESS_NOTIFICATION"),
                eq(RabbitConstants.NOTIFICATION_EXCHANGE),
                eq(RabbitConstants.NOTIFICATION_SUCCESS_ROUTING_KEY),
                eq(event)
        )).thenReturn(baseEvent);

        dispatcher.publishSuccessNotification(event);

        verify(outboxService).saveBaseEvent(
                "SUCCESS_NOTIFICATION",
                RabbitConstants.NOTIFICATION_EXCHANGE,
                RabbitConstants.NOTIFICATION_SUCCESS_ROUTING_KEY,
                event
        );
        verify(outboxService).publish(baseEvent);
    }

    @Test
    void shouldPublishFailureNotification() {
        FailureNotificationEvent event = mock(FailureNotificationEvent.class);
        BaseEvent baseEvent = new BaseEvent();

        when(outboxService.saveBaseEvent(
                eq("FAILURE_NOTIFICATION"),
                eq(RabbitConstants.NOTIFICATION_EXCHANGE),
                eq(RabbitConstants.NOTIFICATION_FAILURE_ROUTING_KEY),
                eq(event)
        )).thenReturn(baseEvent);

        dispatcher.publishFailureNotification(event);

        verify(outboxService).saveBaseEvent(
                "FAILURE_NOTIFICATION",
                RabbitConstants.NOTIFICATION_EXCHANGE,
                RabbitConstants.NOTIFICATION_FAILURE_ROUTING_KEY,
                event
        );
        verify(outboxService).publish(baseEvent);
    }
}
