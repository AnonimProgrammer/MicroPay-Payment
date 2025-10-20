package com.micropay.payment.service.coordinator;

import com.micropay.payment.dto.transaction.TransactionCreatedEvent;
import com.micropay.payment.dto.wallet.credit.WalletCreditFailedEvent;
import com.micropay.payment.dto.wallet.credit.WalletCreditedEvent;
import com.micropay.payment.dto.wallet.debit.WalletDebitFailedEvent;
import com.micropay.payment.dto.wallet.debit.WalletDebitedEvent;
import com.micropay.payment.dto.wallet.refund.WalletRefundedEvent;

public interface PaymentCoordinatorService {

    void handleTransactionCreatedEvent(TransactionCreatedEvent transactionCreatedEvent);

    void handleWalletDebitedEvent(WalletDebitedEvent walletDebitedEvent);

    void handleWalletCreditedEvent(WalletCreditedEvent walletCreditedEvent);

    void handleWalletDebitFailedEvent(WalletDebitFailedEvent walletDebitFailedEvent);

    void handleWalletCreditFailedEvent(WalletCreditFailedEvent walletCreditFailedEvent);

    void handleWalletRefundedEvent(WalletRefundedEvent walletRefundedEvent);

}
