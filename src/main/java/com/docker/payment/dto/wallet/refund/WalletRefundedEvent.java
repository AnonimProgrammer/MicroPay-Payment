package com.docker.payment.dto.wallet.refund;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletRefundedEvent {

    private UUID transactionId;
    private Long walletId;
    private Long userId;
    private BigDecimal amount;

    public WalletRefundedEvent() {}
    public WalletRefundedEvent(UUID transactionId, Long walletId, Long userId, BigDecimal amount) {
        this.transactionId = transactionId;
        this.walletId = walletId;
        this.userId = userId;
        this.amount = amount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
    public Long getWalletId() {
        return walletId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public Long getUserId() {
        return userId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }
    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "WalletRefundedEvent {" +
                "transactionId = " + transactionId +
                ", walletId = " + walletId +
                ", amount = " + amount +
                ", userId = " + userId +
                '}';
    }
}
