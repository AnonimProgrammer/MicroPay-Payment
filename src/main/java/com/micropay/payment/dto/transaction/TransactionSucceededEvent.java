package com.micropay.payment.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TransactionSucceededEvent {

    private UUID transactionId;

    @Override
    public String toString() {
        return "TransactionSucceededEvent {" +
                "transactionId = " + transactionId +
                '}';
    }
}
