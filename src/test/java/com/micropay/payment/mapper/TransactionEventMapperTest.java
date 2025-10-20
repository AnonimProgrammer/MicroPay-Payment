package com.micropay.payment.mapper;

import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.dto.transaction.TransactionFailedEvent;
import com.micropay.payment.dto.transaction.TransactionSucceededEvent;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.payment.entity.Payment;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionEventMapperTest {

    private TransactionEventMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TransactionEventMapperImpl();
    }

    @Test
    void mapToInitiateTransactionEvent_ShouldMapAllFields() {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setAmount(new BigDecimal("150"));
        payment.setSource("source");
        payment.setSourceType(EndpointType.CARD);
        payment.setDestination("destination");
        payment.setDestinationType(EndpointType.WALLET);
        payment.setType(TransactionType.TOP_UP);

        InitiateTransactionEvent event = mapper.mapToInitiateTransactionEvent(payment);

        assertThat(event).isNotNull();
        assertThat(event.eventId()).isNotNull();
        assertThat(event.paymentId()).isEqualTo(payment.getId());
        assertThat(event.amount()).isEqualTo(payment.getAmount());
        assertThat(event.source()).isEqualTo(payment.getSource());
        assertThat(event.sourceType()).isEqualTo(payment.getSourceType());
        assertThat(event.destination()).isEqualTo(payment.getDestination());
        assertThat(event.destinationType()).isEqualTo(payment.getDestinationType());
        assertThat(event.type()).isEqualTo(payment.getType());
    }

    @Test
    void mapToTransactionSucceededEvent_ShouldMapCorrectly() {
        PaymentModel payment = new PaymentModel();
        UUID transactionId = UUID.randomUUID();
        payment.setTransactionId(transactionId);

        TransactionSucceededEvent event = mapper.mapToTransactionSucceededEvent(payment);

        assertThat(event).isNotNull();
        assertThat(event.eventId()).isNotNull();
        assertThat(event.transactionId()).isEqualTo(transactionId);
    }

    @Test
    void mapToTransactionFailedEvent_ShouldMapCorrectly() {
        PaymentModel payment = new PaymentModel();
        UUID transactionId = UUID.randomUUID();
        payment.setTransactionId(transactionId);

        String failureReason = "Insufficient funds";

        TransactionFailedEvent event = mapper.mapToTransactionFailedEvent(payment, failureReason);

        assertThat(event).isNotNull();
        assertThat(event.eventId()).isNotNull();
        assertThat(event.transactionId()).isEqualTo(transactionId);
        assertThat(event.failureReason()).isEqualTo(failureReason);
    }

    @Test
    void mapToTransactionFailedEvent_ShouldHandleNullPayment() {
        String failureReason = "Network error";

        TransactionFailedEvent event = mapper.mapToTransactionFailedEvent(null, failureReason);

        assertThat(event).isNotNull();
        assertThat(event.eventId()).isNotNull();
        assertThat(event.transactionId()).isNull();
        assertThat(event.failureReason()).isEqualTo(failureReason);
    }

    @Test
    void mapToTransactionFailedEvent_ShouldReturnNullIfBothNull() {
        TransactionFailedEvent event = mapper.mapToTransactionFailedEvent(null, null);

        assertThat(event).isNull();
    }
}
