package com.micropay.payment.dto.wallet.credit;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreditWalletEvent {

    private Long paymentId;
    private Long walletId;
    private BigDecimal amount;

    @Override
    public String toString() {
        return "CreditWalletEvent {" +
                "paymentId = " + paymentId +
                ", walletId = " + walletId +
                ", amount = " + amount +
                '}';
    }
}