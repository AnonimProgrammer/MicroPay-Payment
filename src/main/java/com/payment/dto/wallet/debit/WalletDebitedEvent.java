package com.payment.dto.wallet.debit;

import java.math.BigDecimal;

public class WalletDebitedEvent {

    private Long paymentId;
    private Long walletId;
    private Long userId;
    private BigDecimal amount;

    public WalletDebitedEvent() {}
    public WalletDebitedEvent(Long paymentId, Long walletId, Long userId, BigDecimal amount) {
        this.paymentId = paymentId;
        this.walletId = walletId;
        this.userId = userId;
        this.amount = amount;
    }

    public Long getPaymentId() {
        return paymentId;
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    @Override
    public String toString() {
        return "WalletDebitedEvent{" +
                "paymentId=" + paymentId +
                ", walletId=" + walletId +
                ", userId=" + userId +
                ", amount=" + amount +
                '}';
    }
}
