package com.payment.dto.wallet.refund;

import java.math.BigDecimal;

public class RefundWalletEvent {

    private Long paymentId;
    private Long walletId;
    private BigDecimal amount;

    public RefundWalletEvent() {}
    public RefundWalletEvent(Long paymentId, Long walletId, BigDecimal amount) {
        this.paymentId = paymentId;
        this.walletId = walletId;
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
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "RefundWalletEvent {" +
                "paymentId = " + paymentId +
                ", walletId = " + walletId +
                ", amount = " + amount +
                '}';
    }
}