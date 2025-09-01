package com.docker.payment.dto.payment.internal.response;

import com.docker.payment.model.wallet.ReservationStatus;

import java.math.BigDecimal;

public class ReservationResponse {

    private Long walletId;
    private Long paymentId;
    private BigDecimal reservedAmount;
    private BigDecimal availableBalance;
    private ReservationStatus status;

    public ReservationResponse() {}
    public ReservationResponse(Builder builder) {
        this.walletId = builder.walletId;
        this.paymentId = builder.paymentId;
        this.reservedAmount = builder.reservedAmount;
        this.availableBalance = builder.availableBalance;
        this.status = builder.status;
    }

    public static class  Builder {
        private Long walletId;
        private Long paymentId;
        private BigDecimal reservedAmount;
        private BigDecimal availableBalance;
        private ReservationStatus status;

        public Builder setWalletId(Long walletId) {
            this.walletId = walletId;
            return this;
        }
        public Builder setPaymentId(Long paymentId) {
            this.paymentId = paymentId;
            return this;
        }
        public Builder setReservedAmount(BigDecimal reservedAmount) {
            this.reservedAmount = reservedAmount;
            return this;
        }
        public Builder setAvailableBalance(BigDecimal availableBalance) {
            this.availableBalance = availableBalance;
            return this;
        }
        public Builder setStatus(ReservationStatus status) {
            this.status = status;
            return this;
        }
        public ReservationResponse build() {
            return new ReservationResponse(this);
        }
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(BigDecimal reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public String toString() {
        return "ReservationResponse{" +
                "walletId=" + walletId +
                ", paymentId=" + paymentId +
                ", reservedAmount=" + reservedAmount +
                ", availableBalance=" + availableBalance +
                ", status=" + status +
                '}';
    }
}
