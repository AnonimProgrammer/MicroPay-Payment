package com.docker.payment.dto.notification;

import java.util.UUID;

public class SuccessNotificationEvent {

    private UUID transactionId;
    private Long senderWalletId;
    private Long receiverWalletId;
    private Double amount;
    private String message;

    public SuccessNotificationEvent() {}
    public SuccessNotificationEvent(UUID transactionId, Long senderWalletId, Long receiverWalletId, Double amount, String message) {
        this.transactionId = transactionId;
        this.senderWalletId = senderWalletId;
        this.receiverWalletId = receiverWalletId;
        this.amount = amount;
        this.message = message;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
    public Long getSenderWalletId() {
        return senderWalletId;
    }
    public Long getReceiverWalletId() {
        return receiverWalletId;
    }
    public Double getAmount() {
        return amount;
    }
    public String getMessage() {
        return message;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }
    public void setSenderWalletId(Long senderWalletId) {
        this.senderWalletId = senderWalletId;
    }
    public void setReceiverWalletId(Long receiverWalletId) {
        this.receiverWalletId = receiverWalletId;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SuccessNotificationEvent {" +
                "transactionId = " + transactionId +
                ", senderWalletId = " + senderWalletId +
                ", receiverWalletId = " + receiverWalletId +
                ", amount = " + amount +
                ", message = '" + message + '\'' +
                '}';
    }
}
