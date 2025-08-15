package com.docker.payment.dto.transaction;

import java.util.UUID;

public class TransactionFailedEvent {

    private UUID transactionId;

    private String failureReason;

    public TransactionFailedEvent(UUID transactionId, String failureReason) {
        this.transactionId = transactionId;
        this.failureReason = failureReason;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @Override
    public String toString() {
        return "TransactionFailedEvent{" +
                "transactionId=" + transactionId +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }
}
