package com.micropay.payment.repo;

import com.micropay.payment.model.payment.PaymentStatus;
import com.micropay.payment.model.payment.entity.Payment;
import com.micropay.payment.model.transaction.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepositoryExtension {

    List<Payment> findPaymentsByWalletIdAndCursor(String walletId, LocalDateTime cursor, int limit);

    List<Payment> findPayments(
            Long walletId,
            TransactionType transactionType,
            PaymentStatus paymentStatus,
            LocalDateTime cursorDate,
            int limit
    );
}
