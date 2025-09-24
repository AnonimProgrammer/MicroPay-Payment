package com.micropay.payment.dto.wallet.credit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletCreditFailedEvent {

    private Long paymentId;
    private Long walletId;
    private String failureReason;

    @Override
    public String toString() {
        return "WalletCreditFailedEvent {" +
                "paymentId = " + paymentId +
                ", walletId = " + walletId +
                ", failureReason = '" + failureReason + '\'' +
                '}';
    }
}