package com.payment.dto.wallet.debit;

import java.math.BigDecimal;

public class DebitWalletEvent {

    private Long paymentId;
    private Long walletId;
    private BigDecimal amount;

    public DebitWalletEvent() {}
    public DebitWalletEvent(Long paymentId, Long walletId, BigDecimal amount) {
        this.paymentId = paymentId;
        this.walletId = walletId;
        this.amount = amount;
    }

    public Long getWalletId() {
        return walletId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public Long getPaymentId() {
        return paymentId;
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
        return "DebitWalletEvent{" +
                "paymentId=" + paymentId +
                ", walletId=" + walletId +
                ", amount=" + amount +
                '}';
    }
}
