package com.payment.dto.wallet.debit;

public class WalletDebitFailedEvent {

    private Long paymentId;
    private Long walletId;
    private Long userId;
    private String failureReason;

    public WalletDebitFailedEvent() {}
    public WalletDebitFailedEvent(Long paymentId, Long walletId, Long userId, String failureReason) {
        this.paymentId = paymentId;
        this.walletId = walletId;
        this.userId = userId;
        this.failureReason = failureReason;
    }

    public Long getPaymentId() {
        return paymentId;
    }
    public Long getWalletId() {
        return walletId;
    }
    public String getFailureReason() {
        return failureReason;
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
    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @Override
    public String toString() {
        return "WalletDebitFailedEvent{" +
                "paymentId=" + paymentId +
                ", walletId=" + walletId +
                ", userId=" + userId +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }
}
