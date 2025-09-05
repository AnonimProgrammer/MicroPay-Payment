package com.payment.dto.wallet.credit;

import java.math.BigDecimal;

public class WalletCreditedEvent {

    private Long paymentId;
    private Long walletId;
    private Long userId;
    private BigDecimal amount;

    public WalletCreditedEvent() {}
    public WalletCreditedEvent(Long paymentId, Long walletId, Long userId, BigDecimal amount) {
        this.paymentId = paymentId;
        this.walletId = walletId;
        this.userId = userId;
        this.amount = amount;
    }

    public Long getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    public Long getWalletId() {
        return walletId;
    }
    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "WalletCreditedEvent {" +
                "paymentId = " + paymentId +
                ", walletId = " + walletId +
                ", userId = " + userId +
                ", amount = " + amount +
                '}';
    }
}