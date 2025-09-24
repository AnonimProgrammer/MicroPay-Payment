package com.micropay.payment.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TransactionFailedEvent {

    private UUID transactionId;
    private String failureReason;

    @Override
    public String toString() {
        return "TransactionFailedEvent{" +
                "transactionId=" + transactionId +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }
}
