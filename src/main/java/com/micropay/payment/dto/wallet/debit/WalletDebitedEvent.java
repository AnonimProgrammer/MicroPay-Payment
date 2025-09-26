package com.micropay.payment.dto.wallet.debit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDebitedEvent {

    private Long paymentId;
    private Long walletId;
    private BigDecimal amount;

    @Override
    public String toString() {
        return "WalletDebitedEvent{" +
                "paymentId=" + paymentId +
                ", walletId=" + walletId +
                ", amount=" + amount +
                '}';
    }
}
