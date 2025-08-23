package com.docker.payment.service;

import com.docker.payment.dto.payment.internal.request.PaymentRequest;
import com.docker.payment.dto.payment.internal.response.PaymentResponse;
import com.docker.payment.dto.transaction.InitiateTransactionEvent;
import com.docker.payment.dto.transaction.TransactionCreatedEvent;
import com.docker.payment.dto.wallet.credit.CreditWalletEvent;
import com.docker.payment.dto.wallet.debit.DebitWalletEvent;
import com.docker.payment.dto.wallet.debit.WalletDebitedEvent;
import com.docker.payment.exception.InvalidPaymentRequestException;
import com.docker.payment.messaging.transaction.TransactionMessagePublisher;
import com.docker.payment.messaging.wallet.WalletMessagePublisher;
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
    private final WalletMessagePublisher walletMessagePublisher;

    public PaymentCoordinatorService(WithdrawalProcessorService withdrawalProcessorService,
                                     TopUpProcessorService topUpProcessorService,
                                     PaymentDataAccessService paymentDataAccessService,
                                     TransactionMessagePublisher transactionMessagePublisher, WalletMessagePublisher walletMessagePublisher) {
        this.withdrawalProcessorService = withdrawalProcessorService;
        this.topUpProcessorService = topUpProcessorService;
        this.paymentDataAccessService = paymentDataAccessService;
        this.transactionMessagePublisher = transactionMessagePublisher;
        this.walletMessagePublisher = walletMessagePublisher;
    }

    public PaymentResponse processTopUpRequest(PaymentRequest paymentRequest) {
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

    public PaymentResponse processWithdrawalRequest(PaymentRequest paymentRequest) {
        if (paymentRequest.getType().compareTo(TransactionType.WITHDRAWAL) != 0) {
            throw new InvalidPaymentRequestException("Invalid transaction type for withdrawal: " + paymentRequest.getType());
        }
        PaymentModel payment = paymentDataAccessService.savePayment(paymentRequest);

        InitiateTransactionEvent initiateTransactionEvent = DtoBuilder.buildInitiateTransactionEvent(payment);
        transactionMessagePublisher.publishInitiateTransactionEvent(initiateTransactionEvent);

        return DtoBuilder.buildPaymentResponse(payment);
    }

    public PaymentResponse processTransferRequest(PaymentRequest paymentRequest) {
        if (paymentRequest.getType().compareTo(TransactionType.TRANSFER) != 0) {
            throw new InvalidPaymentRequestException("Invalid transaction type for transfer: " + paymentRequest.getType());
        }
        PaymentValidator.validateWalletId(paymentRequest.getSource());
        PaymentValidator.validateWalletId(paymentRequest.getDestination());

        PaymentModel payment = paymentDataAccessService.savePayment(paymentRequest);

        InitiateTransactionEvent initiateTransactionEvent = DtoBuilder.buildInitiateTransactionEvent(payment);
        transactionMessagePublisher.publishInitiateTransactionEvent(initiateTransactionEvent);

        return DtoBuilder.buildPaymentResponse(payment);
    }

    public void handleTransactionCreatedEvent(TransactionCreatedEvent transactionCreatedEvent) {
        PaymentModel payment = paymentDataAccessService.updatePaymentWithTransactionId(
                transactionCreatedEvent.getPaymentId(),
                transactionCreatedEvent.getTransactionId()
        );
        if (payment.getType().compareTo(TransactionType.TOP_UP) == 0) {
            CreditWalletEvent creditWalletEvent = DtoBuilder.buildCreditWalletEvent(payment);
            walletMessagePublisher.publishCreditWalletEvent(creditWalletEvent);
            return;
        }
        DebitWalletEvent debitWalletEvent = DtoBuilder.buildDebitWalletEvent(payment);
        walletMessagePublisher.publishDebitWalletEvent(debitWalletEvent);
    }

    public void handleWalletDebitedEvent(WalletDebitedEvent walletDebitedEvent) {

    }
}
