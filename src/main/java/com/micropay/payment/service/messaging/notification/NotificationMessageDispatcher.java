package com.micropay.payment.service.messaging.notification;

import com.micropay.payment.dto.notification.FailureNotificationEvent;
import com.micropay.payment.dto.notification.SuccessNotificationEvent;

public interface NotificationMessageDispatcher {

    void publishSuccessNotification(SuccessNotificationEvent event);

    void publishFailureNotification(FailureNotificationEvent event);

}
