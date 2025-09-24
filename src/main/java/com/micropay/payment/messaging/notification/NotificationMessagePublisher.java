package com.micropay.payment.messaging.notification;

import com.micropay.payment.config.RabbitConstants;
import com.micropay.payment.dto.notification.FailureNotificationEvent;
import com.micropay.payment.dto.notification.SuccessNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final static Logger logger = LoggerFactory.getLogger(NotificationMessagePublisher.class);

    public void publishSuccessNotification(SuccessNotificationEvent SuccessNotificationEvent) {
        logger.info("[NotificationMessagePublisher] - Publishing SuccessNotificationEvent: {}", SuccessNotificationEvent);
        rabbitTemplate.convertAndSend(
                RabbitConstants.NOTIFICATION_EXCHANGE,
                RabbitConstants.NOTIFICATION_SUCCESS_ROUTING_KEY, SuccessNotificationEvent
        );
    }

    public void publishFailureNotification(FailureNotificationEvent failureNotificationEvent) {
        logger.info("[NotificationMessagePublisher] - Publishing failure notification: {}", failureNotificationEvent);
        rabbitTemplate.convertAndSend(
                RabbitConstants.NOTIFICATION_EXCHANGE,
                RabbitConstants.NOTIFICATION_FAILURE_ROUTING_KEY, failureNotificationEvent
        );
    }
}
