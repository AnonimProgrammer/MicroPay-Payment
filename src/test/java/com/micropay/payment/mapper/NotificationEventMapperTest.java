package com.micropay.payment.mapper;

import com.micropay.payment.config.NotificationConstants;
import com.micropay.payment.dto.notification.FailureNotificationEvent;
import com.micropay.payment.dto.notification.SuccessNotificationEvent;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.transaction.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationEventMapperTest {

    private NotificationEventMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new NotificationEventMapper();
    }

    @Test
    void mapToSuccessNotificationEvent_ShouldMapCorrectly() {
        PaymentModel payment = new PaymentModel();
        payment.setAmount(BigDecimal.valueOf(123.4600));
        payment.setType(TransactionType.TOP_UP);

        String subscriberId = "42";
        String message = "Payment of ";

        SuccessNotificationEvent event = mapper.mapToSuccessNotificationEvent(payment, subscriberId, message);

        assertThat(event).isNotNull();
        assertThat(event.eventId()).isNotNull();
        assertThat(event.walletId()).isEqualTo(42L);
        assertThat(event.title()).isEqualTo("TOP_UP");
        assertThat(event.content()).isEqualTo("Payment of 123.46AZN");
    }

    @Test
    void mapToFailureNotificationEvent_ShouldMapCorrectly() {
        PaymentModel payment = new PaymentModel();
        payment.setAmount(BigDecimal.valueOf(78.9));
        payment.setType(TransactionType.TOP_UP);

        String subscriberId = "99";

        FailureNotificationEvent event = mapper.mapToFailureNotificationEvent(payment, subscriberId);

        assertThat(event).isNotNull();
        assertThat(event.eventId()).isNotNull();
        assertThat(event.walletId()).isEqualTo(99L);
        assertThat(event.title()).isEqualTo("FAILURE");
        assertThat(event.content()).isEqualTo(NotificationConstants.OPERATION_FAILED_MESSAGE + "78.90AZN");
    }

}
