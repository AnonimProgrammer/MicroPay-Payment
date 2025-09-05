package com.payment.dto.transaction;

import java.util.UUID;

public class TransactionSucceededEvent {

    private UUID transactionId;

    public TransactionSucceededEvent(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "TransactionSucceededEvent {" +
                "transactionId = " + transactionId +
                '}';
    }
}
