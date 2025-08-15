package com.docker.payment.dto.wallet.debit;

import java.util.UUID;

public class WalletDebitFailedEvent {

    private UUID transactionId;
    private Long walletId;
    private String failureReason;

    public WalletDebitFailedEvent() {}
    public WalletDebitFailedEvent(UUID transactionId, Long walletId, String failureReason) {
        this.transactionId = transactionId;
        this.walletId = walletId;
        this.failureReason = failureReason;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
    public Long getWalletId() {
        return walletId;
    }
    public String getFailureReason() {
        return failureReason;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }
    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @Override
    public String toString() {
        return "WalletDebitFailedEvent {" +
                "transactionId = " + transactionId +
                ", walletId = " + walletId +
                ", failureReason = '" + failureReason + '\'' +
                '}';
    }
}
