package com.docker.payment.service;

import com.docker.payment.dto.payment.internal.PaymentRequest;
import com.docker.payment.dto.payment.internal.PaymentResponse;
import com.docker.payment.dto.transaction.InitiateTransactionEvent;
import com.docker.payment.exception.InvalidPaymentRequestException;
import com.docker.payment.messaging.transaction.TransactionMessagePublisher;
import com.docker.payment.model.payment.PaymentModel;
import com.docker.payment.model.transaction.TransactionType;
import com.docker.payment.service.processor.PaymentProcessor;
import com.docker.payment.service.processor.top_up.TopUpProcessorService;
import com.docker.payment.service.processor.withdrawal.WithdrawalProcessorService;
import com.docker.payment.util.DtoBuilder;
import org.springframework.stereotype.Service;

@Service
public class PaymentCoordinatorService {

    private final WithdrawalProcessorService withdrawalProcessorService;
    private final TopUpProcessorService topUpProcessorService;
    private final PaymentDataAccessService paymentDataAccessService;
    private final TransactionMessagePublisher transactionMessagePublisher;

    public PaymentCoordinatorService(WithdrawalProcessorService withdrawalProcessorService,
                                     TopUpProcessorService topUpProcessorService,
                                     PaymentDataAccessService paymentDataAccessService,
                                     TransactionMessagePublisher transactionMessagePublisher) {
        this.withdrawalProcessorService = withdrawalProcessorService;
        this.topUpProcessorService = topUpProcessorService;
        this.paymentDataAccessService = paymentDataAccessService;
        this.transactionMessagePublisher = transactionMessagePublisher;
    }

    public PaymentResponse handleTopUpRequest(PaymentRequest paymentRequest) {
        if (paymentRequest.getType().compareTo(TransactionType.TOP_UP) != 0) {
            throw new InvalidPaymentRequestException("Invalid transaction type for top-up: " + paymentRequest.getType());
        }
        PaymentProcessor paymentProcessor = topUpProcessorService.getPaymentProcessor(paymentRequest);
        paymentProcessor.processPayment(paymentRequest);

        PaymentModel payment = paymentDataAccessService.savePayment(paymentRequest);

        InitiateTransactionEvent initiateTransactionEvent = DtoBuilder.buildInitiateTransactionEvent(payment);
        transactionMessagePublisher.publishInitiateTransactionEvent(initiateTransactionEvent);

        return DtoBuilder.buildPaymentResponse(payment);
    }

    public PaymentResponse handleWithdrawalRequest(PaymentRequest paymentRequest) {
        if (paymentRequest.getType().compareTo(TransactionType.WITHDRAWAL) != 0) {
            throw new InvalidPaymentRequestException("Invalid transaction type for withdrawal: " + paymentRequest.getType());
        }
        PaymentModel payment = paymentDataAccessService.savePayment(paymentRequest);

        InitiateTransactionEvent initiateTransactionEvent = DtoBuilder.buildInitiateTransactionEvent(payment);
        transactionMessagePublisher.publishInitiateTransactionEvent(initiateTransactionEvent);

        return DtoBuilder.buildPaymentResponse(payment);
    }
}
