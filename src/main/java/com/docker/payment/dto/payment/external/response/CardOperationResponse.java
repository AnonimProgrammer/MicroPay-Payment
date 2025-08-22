package com.docker.payment.dto.payment.external.response;

import java.math.BigDecimal;

public class CardOperationResponse {
    private String transactionId;
    private String message;
    private BigDecimal amountWithdrawn;
    private String currency;

    public CardOperationResponse() {}
    public CardOperationResponse(String transactionId, String message, BigDecimal amountWithdrawn, String currency) {
        this.transactionId = transactionId;
        this.message = message;
        this.amountWithdrawn = amountWithdrawn;
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

    public BigDecimal getAmountWithdrawn() {
        return amountWithdrawn;
    }

    public void setAmountWithdrawn(BigDecimal amountWithdrawn) {
        this.amountWithdrawn = amountWithdrawn;
    }

    @Override
    public String toString() {
        return "CardOperationResponse{" +
                "transactionId='" + transactionId + '\'' +
                ", message='" + message + '\'' +
                ", amountWithdrawn=" + amountWithdrawn +
                ", currency='" + currency + '\'' +
                '}';
    }
}
