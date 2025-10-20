package com.micropay.payment.service.processor.transfer;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.request.ReservationRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.exception.InvalidPaymentRequestException;
import com.micropay.payment.mapper.PaymentMapper;
import com.micropay.payment.mapper.TransactionEventMapper;
import com.micropay.payment.service.messaging.transaction.TransactionMessageDispatcher;
import com.micropay.payment.model.payment.entity.Payment;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.payment.PaymentDataAccessService;
import com.micropay.payment.validator.PaymentValidator;
import com.micropay.payment.service.adapter.WalletServiceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferProcessorServiceImpl implements TransferProcessorService {

    private final PaymentDataAccessService paymentDataAccessService;
    private final TransactionMessageDispatcher transactionMessageDispatcher;
    private final TransactionEventMapper transactionEventMapper;
    private final PaymentMapper paymentMapper;
    private final WalletServiceAdapter walletServiceAdapter;
    private final PaymentValidator paymentValidator;

    @Override
    public PaymentResponse processPaymentRequest(PaymentRequest paymentRequest) {
        log.info("Processing payment request.");

        validateTransferRequest(paymentRequest);

        Payment payment = paymentDataAccessService.savePayment(paymentRequest);
        reserveBalance(payment);

        InitiateTransactionEvent initiateTransactionEvent = transactionEventMapper.mapToInitiateTransactionEvent(payment);
        transactionMessageDispatcher.publishInitiateTransactionEvent(initiateTransactionEvent);

        return paymentMapper.toResponse(payment);
    }

    private void validateTransferRequest(PaymentRequest paymentRequest) {
        paymentValidator.validateTransactionType(paymentRequest, TransactionType.TRANSFER);
        paymentValidator.validateWalletId(paymentRequest.getSource());
        paymentValidator.validateWalletId(paymentRequest.getDestination());

        if (paymentRequest.getSourceType() != EndpointType.WALLET ||
                paymentRequest.getDestinationType() != EndpointType.WALLET) {
            throw new InvalidPaymentRequestException("Invalid endpoint types for transfer.");
        }
        if (paymentRequest.getSource().equals(paymentRequest.getDestination())) {
            throw new InvalidPaymentRequestException
                    ("Identical source and destination wallet IDs are not allowed for transfer payments.");
        }
    }

    private void reserveBalance(Payment payment) {
        walletServiceAdapter.reserveBalance(
                Long.valueOf(payment.getSource()),
                new ReservationRequest(payment.getId(), payment.getAmount()));
    }
}
