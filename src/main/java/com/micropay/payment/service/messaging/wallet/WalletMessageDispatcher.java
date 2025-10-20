package com.micropay.payment.service.messaging.wallet;

import com.micropay.payment.dto.wallet.credit.CreditWalletEvent;
import com.micropay.payment.dto.wallet.debit.DebitWalletEvent;
import com.micropay.payment.dto.wallet.refund.RefundWalletEvent;

public interface WalletMessageDispatcher {

    void publishDebitWalletEvent(DebitWalletEvent event);

    void publishCreditWalletEvent(CreditWalletEvent event);

    void publishRefundWalletEvent(RefundWalletEvent event);

}
