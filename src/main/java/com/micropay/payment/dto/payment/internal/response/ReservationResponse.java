package com.micropay.payment.dto.payment.internal.response;

import com.micropay.payment.model.wallet.ReservationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ReservationResponse {

    private Long walletId;
    private Long paymentId;
    private BigDecimal reservedAmount;
    private BigDecimal availableBalance;
    private ReservationStatus status;

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
