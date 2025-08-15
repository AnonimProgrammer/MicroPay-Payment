package com.docker.payment.messaging.notification;

import com.docker.payment.dto.notification.FailureNotificationEvent;
import com.docker.payment.dto.notification.SuccessNotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessagePublisher {

    @Value("${rabbitmq.exchange.notification}")
    private String notificationExchange;
    @Value("${rabbitmq.routing-key.notification.success}")
    private String notificationSuccessRoutingKey;
    @Value("${rabbitmq.routing-key.notification.failure}")
    private String notificationFailureRoutingKey;

    private final RabbitTemplate rabbitTemplate;
    private final static Logger logger = LoggerFactory.getLogger(NotificationMessagePublisher.class);

    public NotificationMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishNotificationSuccess(SuccessNotificationEvent SuccessNotificationEvent) {
        logger.info("[NotificationMessagePublisher] - Publishing SuccessNotificationEvent: {}", SuccessNotificationEvent);
        rabbitTemplate.convertAndSend(notificationExchange, notificationSuccessRoutingKey, SuccessNotificationEvent);
    }

    public void publishNotificationFailure(FailureNotificationEvent failureNotificationEvent) {
        logger.error("[NotificationMessagePublisher] - Publishing failure notification: {}", failureNotificationEvent);
        rabbitTemplate.convertAndSend(notificationExchange, notificationFailureRoutingKey, failureNotificationEvent);
    }
}
