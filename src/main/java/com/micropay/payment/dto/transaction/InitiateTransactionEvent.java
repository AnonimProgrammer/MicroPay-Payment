package com.micropay.payment.dto.transaction;

import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record InitiateTransactionEvent(
        UUID eventId,
        Long paymentId,
        BigDecimal amount,
        String source,
        EndpointType sourceType,
        String destination,
        EndpointType destinationType,
        TransactionType type
) {
    public String toString() {
        return "InitiateTransactionEvent{" +
                "eventId=" + eventId +
                ", paymentId=" + paymentId +
                ", amount=" + amount +
                ", sourceType=" + sourceType +
                ", destinationType=" + destinationType +
                ", type=" + type +
                '}';
    }
}
