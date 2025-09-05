package com.payment.dto.wallet.credit;

public class WalletCreditFailedEvent {

    private Long paymentId;
    private Long walletId;
    private String failureReason;

    public WalletCreditFailedEvent() {}
    public WalletCreditFailedEvent(Long paymentId, Long walletId, String failureReason) {
        this.paymentId = paymentId;
        this.walletId = walletId;
        this.failureReason = failureReason;
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
    public String getFailureReason() {
        return failureReason;
    }
    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @Override
    public String toString() {
        return "WalletCreditFailedEvent {" +
                "paymentId = " + paymentId +
                ", walletId = " + walletId +
                ", failureReason = '" + failureReason + '\'' +
                '}';
    }
}