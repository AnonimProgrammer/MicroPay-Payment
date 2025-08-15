package com.docker.payment.dto.wallet.debit;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletDebitedEvent {

    private UUID transactionId;
    private Long walletId;
    private BigDecimal amount;

    public WalletDebitedEvent() {}
    public WalletDebitedEvent(UUID transactionId, Long walletId, BigDecimal amount) {
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
        return "WalletDebitedEvent {" +
                "transactionId = " + transactionId +
                ", walletId = " + walletId +
                ", amount = " + amount +
                '}';
    }
}
