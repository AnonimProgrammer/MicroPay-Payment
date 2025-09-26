package com.micropay.payment.factory;

import com.micropay.payment.config.NotificationConstants;
import com.micropay.payment.dto.notification.FailureNotificationEvent;
import com.micropay.payment.dto.notification.SuccessNotificationEvent;
import com.micropay.payment.model.payment.PaymentModel;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class NotificationEventFactory {

    public SuccessNotificationEvent createSuccessNotificationEvent(
            PaymentModel payment, String subscriberId, String message
    ) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String content = String.format("%s%sAZN", message, df.format(payment.getAmount()));

        return new SuccessNotificationEvent(
                Long.valueOf(subscriberId),
                payment.getType().toString(),
                content
        );
    }

    public FailureNotificationEvent createFailureNotificationEvent(PaymentModel payment, String subscriberId) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String content = String.format(
                "%s%sAZN", NotificationConstants.OPERATION_FAILED_MESSAGE, df.format(payment.getAmount())
        );

        return new FailureNotificationEvent(
                Long.valueOf(subscriberId),
                "FAILURE",
                content
        );
    }
}