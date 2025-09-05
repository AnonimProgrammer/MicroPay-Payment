package com.payment.dto.payment.external.response;

import java.math.BigDecimal;

public class CardOperationResponse {
    private String transactionId;
    private String message;
    private BigDecimal amount;
    private String currency;

    public CardOperationResponse() {}
    public CardOperationResponse(String transactionId, String message, BigDecimal amountWithdrawn, String currency) {
        this.transactionId = transactionId;
        this.message = message;
        this.amount = amountWithdrawn;
        this.currency = currency;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

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
