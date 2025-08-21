package com.docker.payment.util;

import com.docker.payment.dto.payment.internal.PaymentResponse;
import com.docker.payment.dto.transaction.InitiateTransactionEvent;
import com.docker.payment.dto.wallet.credit.CreditWalletEvent;
import com.docker.payment.dto.wallet.debit.DebitWalletEvent;
import com.docker.payment.model.payment.PaymentModel;

public class DtoBuilder {

    // TRANSACTION EVENTS
    public static InitiateTransactionEvent buildInitiateTransactionEvent(PaymentModel payment) {
        return new InitiateTransactionEvent.Builder()
                .amount(payment.getAmount())
                .source(payment.getSource())
                .sourceType(payment.getSourceType())
                .destination(payment.getDestination())
                .destinationType(payment.getDestinationType())
                .type(payment.getType())
                .paymentId(payment.getId())
                .build();
    }

    // WALLET EVENTS
    public static CreditWalletEvent buildCreditWalletEvent(PaymentModel payment) {
        return new CreditWalletEvent(
                payment.getTransactionId(),
                Long.valueOf(payment.getDestination()),
                payment.getAmount()
        );
    }

    public static DebitWalletEvent buildDebitWalletEvent(PaymentModel payment) {
        return new DebitWalletEvent(
                payment.getTransactionId(),
                Long.valueOf(payment.getSource()),
                payment.getAmount()
        );
    }

    public static PaymentResponse buildPaymentResponse(PaymentModel payment) {
        return new PaymentResponse.Builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .source(payment.getSource())
                .sourceType(payment.getSourceType())
                .destination(payment.getDestination())
                .destinationType(payment.getDestinationType())
                .type(payment.getType())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }


}
