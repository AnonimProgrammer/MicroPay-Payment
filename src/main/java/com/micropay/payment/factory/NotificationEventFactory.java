package com.micropay.payment.factory;

import com.micropay.payment.dto.notification.FailureNotificationEvent;
import com.micropay.payment.dto.notification.SuccessNotificationEvent;
import com.micropay.payment.model.payment.PaymentModel;
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