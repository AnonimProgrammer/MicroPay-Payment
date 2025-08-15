package com.docker.payment.dto.wallet.credit;

import java.math.BigDecimal;
import java.util.UUID;

public class CreditWalletEvent {

    private UUID transactionId;
    private Long walletId;
    private BigDecimal amount;

    public CreditWalletEvent() {}
    public CreditWalletEvent(UUID transactionId, Long walletId, BigDecimal amount) {
        this.transactionId = transactionId;
        this.walletId = walletId;
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

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }
    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CreditWalletEvent {" +
                "transactionId = " + transactionId +
                ", walletId = " + walletId +
                ", amount = " + amount +
                '}';
    }
}

