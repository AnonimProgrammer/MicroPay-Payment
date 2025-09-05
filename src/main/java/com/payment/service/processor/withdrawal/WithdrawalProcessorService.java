package com.payment.service.processor.withdrawal;

import com.payment.dto.payment.internal.request.PaymentRequest;
import com.payment.dto.payment.internal.request.ReservationRequest;
import com.payment.dto.payment.internal.response.PaymentResponse;
import com.payment.dto.transaction.InitiateTransactionEvent;
import com.payment.factory.PaymentDtoFactory;
import com.payment.factory.TransactionEventFactory;
import com.payment.messaging.transaction.TransactionMessagePublisher;
import com.payment.model.payment.CardDetails;
import com.payment.model.payment.PaymentKey;
import com.payment.model.payment.PaymentModel;
import com.payment.model.transaction.EndpointType;
import com.payment.model.transaction.TransactionType;
import com.payment.service.PaymentDataAccessService;
import com.payment.service.PaymentValidator;
import com.payment.service.adapter.WalletServiceAdapter;
import com.payment.service.processor.BaseProcessorService;
import com.payment.service.processor.PaymentProcessor;
import com.payment.service.processor.PaymentProcessorService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WithdrawalProcessorService extends BaseProcessorService implements PaymentProcessorService {

    private final Map<PaymentKey, PaymentProcessor> registry = new HashMap<>();

    private final WalletToCardProcessor walletToCardProcessor;
    private final PaymentDataAccessService paymentDataAccessService;
    private final TransactionMessagePublisher transactionMessagePublisher;
    private final TransactionEventFactory transactionEventFactory;
    private final PaymentDtoFactory paymentDtoFactory;
    private final WalletServiceAdapter walletServiceAdapter;

    public WithdrawalProcessorService(
            WalletToCardProcessor walletToCardProcessor, PaymentDataAccessService paymentDataAccessService,
            TransactionMessagePublisher transactionMessagePublisher, TransactionEventFactory transactionEventFactory,
            PaymentDtoFactory paymentDtoFactory, WalletServiceAdapter walletServiceAdapter
    ) {
        this.walletToCardProcessor = walletToCardProcessor;
        this.paymentDataAccessService = paymentDataAccessService;
        this.transactionMessagePublisher = transactionMessagePublisher;
        this.transactionEventFactory = transactionEventFactory;
        this.paymentDtoFactory = paymentDtoFactory;
        this.walletServiceAdapter = walletServiceAdapter;
    }

    @PostConstruct
    private void initializeRegistry() {
        registry.put(new PaymentKey(TransactionType.WITHDRAWAL, EndpointType.WALLET, EndpointType.CARD), walletToCardProcessor);
    }

    @Override
    public PaymentResponse processPaymentRequest(PaymentRequest paymentRequest) {
        PaymentValidator.validateTransactionType(paymentRequest, TransactionType.WITHDRAWAL);
        PaymentValidator.validateWalletId(paymentRequest.getSource());

        PaymentModel payment = paymentDataAccessService.savePayment(paymentRequest);
        walletServiceAdapter.reserveBalance(
                Long.valueOf(payment.getSource()),
                new ReservationRequest(payment.getId(), payment.getAmount()));

        InitiateTransactionEvent initiateTransactionEvent = transactionEventFactory.createInitiateTransactionEvent(payment);
        transactionMessagePublisher.publishInitiateTransactionEvent(initiateTransactionEvent);

        return paymentDtoFactory.createPaymentResponse(payment);
    }

    public void processWithdrawal(PaymentModel payment) {
        PaymentRequest paymentRequest = paymentDtoFactory
                .createPaymentRequest(payment, new CardDetails(payment.getDestination()));

        PaymentProcessor paymentProcessor = getPaymentProcessor(paymentRequest, registry);
        paymentProcessor.processPayment(paymentRequest);
    }
}
