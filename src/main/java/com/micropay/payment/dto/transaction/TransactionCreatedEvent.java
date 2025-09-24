package com.micropay.payment.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TransactionCreatedEvent {

    private Long paymentId;
    private UUID transactionId;

    @Override
    public String toString() {
        return "TransactionCreatedEvent {" +
                "transactionId = " + transactionId +
                ", paymentId = " + paymentId +
                '}';
    }
}
