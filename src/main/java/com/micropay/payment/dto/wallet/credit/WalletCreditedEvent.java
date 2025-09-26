package com.micropay.payment.dto.wallet.credit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletCreditedEvent {

    private Long paymentId;
    private Long walletId;
    private BigDecimal amount;

    @Override
    public String toString() {
        return "WalletCreditedEvent {" +
                "paymentId = " + paymentId +
                ", walletId = " + walletId +
                ", amount = " + amount +
                '}';
    }
}