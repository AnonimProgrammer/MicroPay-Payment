package com.micropay.payment.dto.wallet.debit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDebitFailedEvent {

    private Long paymentId;
    private Long walletId;
    private String failureReason;

    @Override
    public String toString() {
        return "WalletDebitFailedEvent{" +
                "paymentId=" + paymentId +
                ", walletId=" + walletId +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }
}
