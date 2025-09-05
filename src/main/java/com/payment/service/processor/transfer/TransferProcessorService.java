package com.payment.service.processor.transfer;

import com.payment.dto.payment.internal.request.PaymentRequest;
import com.payment.dto.payment.internal.request.ReservationRequest;
import com.payment.dto.payment.internal.response.PaymentResponse;
import com.payment.dto.transaction.InitiateTransactionEvent;
import com.payment.exception.InvalidPaymentRequestException;
import com.payment.factory.PaymentDtoFactory;
import com.payment.factory.TransactionEventFactory;
import com.payment.messaging.transaction.TransactionMessagePublisher;
import com.payment.model.payment.PaymentModel;
import com.payment.model.transaction.TransactionType;
import com.payment.service.PaymentDataAccessService;
import com.payment.service.PaymentValidator;
import com.payment.service.adapter.WalletServiceAdapter;
import com.payment.service.processor.PaymentProcessorService;
import org.springframework.stereotype.Service;

@Service
public class TransferProcessorService implements PaymentProcessorService {

    private final PaymentDataAccessService paymentDataAccessService;
    private final TransactionMessagePublisher transactionMessagePublisher;
    private final TransactionEventFactory transactionEventFactory;
    private final PaymentDtoFactory paymentDtoFactory;
    private final WalletServiceAdapter walletServiceAdapter;

    public TransferProcessorService(
            PaymentDataAccessService paymentDataAccessService, TransactionMessagePublisher transactionMessagePublisher,
            TransactionEventFactory transactionEventFactory, PaymentDtoFactory paymentDtoFactory,
            WalletServiceAdapter walletServiceAdapter
    ) {
        this.paymentDataAccessService = paymentDataAccessService;
        this.transactionMessagePublisher = transactionMessagePublisher;
        this.transactionEventFactory = transactionEventFactory;
        this.paymentDtoFactory = paymentDtoFactory;
        this.walletServiceAdapter = walletServiceAdapter;
    }

    @Override
    public PaymentResponse processPaymentRequest(PaymentRequest paymentRequest) {
        validateTransferRequest(paymentRequest);

        PaymentModel payment = paymentDataAccessService.savePayment(paymentRequest);
        walletServiceAdapter.reserveBalance(
                Long.valueOf(payment.getSource()),
                new ReservationRequest(payment.getId(), payment.getAmount()));

        InitiateTransactionEvent initiateTransactionEvent = transactionEventFactory.createInitiateTransactionEvent(payment);
        transactionMessagePublisher.publishInitiateTransactionEvent(initiateTransactionEvent);

        return paymentDtoFactory.createPaymentResponse(payment);
    }

    private void validateTransferRequest(PaymentRequest paymentRequest) {
        PaymentValidator.validateTransactionType(paymentRequest, TransactionType.TRANSFER);
        PaymentValidator.validateWalletId(paymentRequest.getSource());
        PaymentValidator.validateWalletId(paymentRequest.getDestination());
        if (paymentRequest.getSource().equals(paymentRequest.getDestination())) {
            throw new InvalidPaymentRequestException("Identical source and destination wallet IDs are not allowed for transfer payments.");
        }
    }
}
