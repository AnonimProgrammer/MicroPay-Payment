package com.micropay.payment.dto.payment.external.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardOperationResponse {
    private String transactionId;
    private String message;
    private BigDecimal amount;
    private String currency;

    @Override
    public String toString() {
        return "CardOperationResponse{" +
                "transactionId='" + transactionId + '\'' +
                ", message='" + message + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
