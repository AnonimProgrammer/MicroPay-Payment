package com.payment.dto.transaction;

import java.util.UUID;

public class TransactionCreatedEvent {

    private Long paymentId;
    private UUID transactionId;

    public TransactionCreatedEvent(UUID transactionId, Long paymentId) {
        this.transactionId = transactionId;
        this.paymentId = paymentId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "TransactionCreatedEvent {" +
                "transactionId = " + transactionId +
                ", paymentId = " + paymentId +
                '}';
    }
}
