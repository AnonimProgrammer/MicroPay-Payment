package com.payment.factory;

import com.payment.dto.notification.FailureNotificationEvent;
import com.payment.dto.notification.SuccessNotificationEvent;
import com.payment.model.payment.PaymentModel;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventFactory {

    public SuccessNotificationEvent createSuccessNotificationEvent(PaymentModel payment, Long userId) {
        String content = String.format("Wallet credited: +%sAZN", payment.getAmount());
        return new SuccessNotificationEvent(
                Long.valueOf(payment.getDestination()),
                userId,
                "TOP-UP",
                content
        );
    }

    public FailureNotificationEvent createFailureNotificationEvent(PaymentModel payment, Long userId) {
        String content = String.format("Operation failed. Amount: %sAZN", payment.getAmount());
        return new FailureNotificationEvent(
                Long.valueOf(payment.getSource()),
                userId,
                "FAILURE",
                content
        );
    }
}