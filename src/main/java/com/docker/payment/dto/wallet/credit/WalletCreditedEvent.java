package com.docker.payment.dto.wallet.credit;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletCreditedEvent {

    private UUID transactionId;
    private Long walletId;
    private Long userId;
    private BigDecimal amount;

    public WalletCreditedEvent() {}
    public WalletCreditedEvent(UUID transactionId, Long walletId, Long userId, BigDecimal amount) {
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
        return "WalletCreditedEvent {" +
                "transactionId = " + transactionId +
                ", walletId = " + walletId +
                ", userId = " + userId +
                ", amount = " + amount +
                '}';
    }
}
