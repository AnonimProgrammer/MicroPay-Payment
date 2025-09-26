package com.micropay.payment.dto.wallet.refund;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletRefundedEvent {

    private Long paymentId;
    private Long walletId;
    private BigDecimal amount;

    @Override
    public String toString() {
        return "WalletRefundedEvent {" +
                "paymentId = " + paymentId +
                ", walletId = " + walletId +
                ", amount = " + amount +
                '}';
    }
}