package com.micropay.payment.dto.wallet.refund;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundWalletEvent {

    private Long paymentId;
    private Long walletId;
    private BigDecimal amount;

    @Override
    public String toString() {
        return "RefundWalletEvent {" +
                "paymentId = " + paymentId +
                ", walletId = " + walletId +
                ", amount = " + amount +
                '}';
    }
}