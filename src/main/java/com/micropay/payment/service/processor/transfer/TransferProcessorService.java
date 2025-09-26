package com.micropay.payment.service.processor.transfer;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.request.ReservationRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.exception.InvalidPaymentRequestException;
import com.micropay.payment.factory.PaymentDtoFactory;
import com.micropay.payment.factory.TransactionEventFactory;
import com.micropay.payment.messaging.transaction.TransactionMessagePublisher;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.PaymentDataAccessService;
import com.micropay.payment.service.PaymentValidator;
import com.micropay.payment.service.adapter.WalletServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferProcessorService {

    private final PaymentDataAccessService paymentDataAccessService;
    private final TransactionMessagePublisher transactionMessagePublisher;
    private final TransactionEventFactory transactionEventFactory;
    private final PaymentDtoFactory paymentDtoFactory;
    private final WalletServiceAdapter walletServiceAdapter;

    public PaymentResponse processPaymentRequest(PaymentRequest paymentRequest) {
        validateTransferRequest(paymentRequest);

        PaymentModel payment = paymentDataAccessService.savePayment(paymentRequest);
        reserveBalance(payment);

        InitiateTransactionEvent initiateTransactionEvent = transactionEventFactory.createInitiateTransactionEvent(payment);
        transactionMessagePublisher.publishInitiateTransactionEvent(initiateTransactionEvent);

        return paymentDtoFactory.createPaymentResponse(payment);
    }

    private void validateTransferRequest(PaymentRequest paymentRequest) {
        PaymentValidator.validateTransactionType(paymentRequest, TransactionType.TRANSFER);
        PaymentValidator.validateWalletId(paymentRequest.getSource());
        PaymentValidator.validateWalletId(paymentRequest.getDestination());

        if (paymentRequest.getSource().equals(paymentRequest.getDestination())) {
            throw new InvalidPaymentRequestException
                    ("Identical source and destination wallet IDs are not allowed for transfer payments.");
        }
    }

    private void reserveBalance(PaymentModel payment) {
        walletServiceAdapter.reserveBalance(
                Long.valueOf(payment.getSource()),
                new ReservationRequest(payment.getId(), payment.getAmount()));
    }
}
