package com.micropay.payment.factory;

import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.dto.transaction.TransactionFailedEvent;
import com.micropay.payment.dto.transaction.TransactionSucceededEvent;
import com.micropay.payment.model.payment.PaymentModel;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventFactory {

    public InitiateTransactionEvent createInitiateTransactionEvent(PaymentModel payment) {
        return new InitiateTransactionEvent.Builder()
                .amount(payment.getAmount())
                .source(payment.getSource())
                .sourceType(payment.getSourceType())
                .destination(payment.getDestination())
                .destinationType(payment.getDestinationType())
                .type(payment.getType())
                .paymentId(payment.getId())
                .build();
    }

    public TransactionSucceededEvent createTransactionSucceededEvent(PaymentModel payment) {
        return new TransactionSucceededEvent(payment.getTransactionId());
    }

    public TransactionFailedEvent createTransactionFailedEvent(PaymentModel payment, String failureReason) {
        return new TransactionFailedEvent(payment.getTransactionId(), failureReason);
    }
}