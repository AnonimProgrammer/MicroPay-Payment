package com.micropay.payment.service.payment;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.response.CursorPage;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.payment.PaymentStatus;
import com.micropay.payment.model.payment.entity.Payment;
import com.micropay.payment.model.transaction.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PaymentDataAccessService {

    Payment savePayment(PaymentRequest paymentRequest);

    PaymentModel updatePaymentWithTransactionId(Long id, UUID transactionId);

    PaymentModel updatePaymentStatus(Long id, PaymentStatus status, String failureReason);

    CursorPage<PaymentResponse> getPaymentHistoryByWalletId(Long walletId, LocalDateTime cursorDate);

    CursorPage<PaymentModel> getPayments(
            TransactionType transactionType, PaymentStatus paymentStatus,
            Long walletId, Integer pageSize, LocalDateTime cursorDate
    );
}
