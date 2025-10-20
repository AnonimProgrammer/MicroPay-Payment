package com.micropay.payment.service.messaging.notification;

import com.micropay.payment.config.RabbitConstants;
import com.micropay.payment.dto.notification.FailureNotificationEvent;
import com.micropay.payment.dto.notification.SuccessNotificationEvent;
import com.micropay.payment.model.event.entity.BaseEvent;
import com.micropay.payment.service.messaging.outbox.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationMessageDispatcherImpl implements NotificationMessageDispatcher {

    private final OutboxService outboxService;

    @Override
    public void publishSuccessNotification(SuccessNotificationEvent event) {
        log.info("Publishing SuccessNotificationEvent: {}", event);
        BaseEvent baseEvent = outboxService.saveBaseEvent(
                "SUCCESS_NOTIFICATION",
                RabbitConstants.NOTIFICATION_EXCHANGE,
                RabbitConstants.NOTIFICATION_SUCCESS_ROUTING_KEY, event
        );
        outboxService.publish(baseEvent);
    }

    @Override
    public void publishFailureNotification(FailureNotificationEvent event) {
        log.info("Publishing FailureNotificationEvent: {}", event);
        BaseEvent baseEvent = outboxService.saveBaseEvent(
                "FAILURE_NOTIFICATION",
                RabbitConstants.NOTIFICATION_EXCHANGE,
                RabbitConstants.NOTIFICATION_FAILURE_ROUTING_KEY, event
        );
        outboxService.publish(baseEvent);
    }
}
