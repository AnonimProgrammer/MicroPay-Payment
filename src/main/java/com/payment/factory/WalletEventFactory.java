package com.payment.factory;

import com.payment.dto.wallet.credit.CreditWalletEvent;
import com.payment.dto.wallet.debit.DebitWalletEvent;
import com.payment.dto.wallet.refund.RefundWalletEvent;
import com.payment.model.payment.PaymentModel;
import org.springframework.stereotype.Component;

@Component
public class WalletEventFactory {

    public CreditWalletEvent createCreditWalletEvent(PaymentModel payment) {
        return new CreditWalletEvent(
                payment.getId(),
                Long.valueOf(payment.getDestination()),
                payment.getAmount()
        );
    }

    public DebitWalletEvent createDebitWalletEvent(PaymentModel payment) {
        return new DebitWalletEvent(
                payment.getId(),
                Long.valueOf(payment.getSource()),
                payment.getAmount()
        );
    }

    public RefundWalletEvent createRefundWalletEvent(PaymentModel payment) {
        return new RefundWalletEvent(
                payment.getId(),
                Long.valueOf(payment.getSource()),
                payment.getAmount()
        );
    }
}