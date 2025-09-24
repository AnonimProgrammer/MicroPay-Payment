package com.micropay.payment.dto.wallet.debit;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DebitWalletEvent {

    private Long paymentId;
    private Long walletId;
    private BigDecimal amount;

    @Override
    public String toString() {
        return "DebitWalletEvent{" +
                "paymentId=" + paymentId +
                ", walletId=" + walletId +
                ", amount=" + amount +
                '}';
    }
}
