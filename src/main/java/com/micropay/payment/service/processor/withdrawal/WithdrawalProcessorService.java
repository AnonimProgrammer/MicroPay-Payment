package com.micropay.payment.service.processor.withdrawal;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.request.ReservationRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.factory.PaymentDtoFactory;
import com.micropay.payment.factory.TransactionEventFactory;
import com.micropay.payment.messaging.transaction.TransactionMessagePublisher;
import com.micropay.payment.model.payment.CardDetails;
import com.micropay.payment.model.payment.PaymentKey;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.PaymentDataAccessService;
import com.micropay.payment.service.PaymentValidator;
import com.micropay.payment.service.adapter.WalletServiceAdapter;
import com.micropay.payment.service.processor.BaseProcessorService;
import com.micropay.payment.service.processor.PaymentProcessor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WithdrawalProcessorService extends BaseProcessorService {

    private final Map<PaymentKey, PaymentProcessor> registry = new HashMap<>();

    private final WalletToCardProcessor walletToCardProcessor;
    private final PaymentDataAccessService paymentDataAccessService;
    private final TransactionMessagePublisher transactionMessagePublisher;
    private final TransactionEventFactory transactionEventFactory;
    private final PaymentDtoFactory paymentDtoFactory;
    private final WalletServiceAdapter walletServiceAdapter;

    @PostConstruct
    private void initializeRegistry() {
        registry.put(new PaymentKey(TransactionType.WITHDRAWAL, EndpointType.WALLET, EndpointType.CARD), walletToCardProcessor);
    }

    public PaymentResponse processPaymentRequest(PaymentRequest paymentRequest) {
        PaymentValidator.validateTransactionType(paymentRequest, TransactionType.WITHDRAWAL);
        PaymentValidator.validateWalletId(paymentRequest.getSource());

        PaymentModel payment = paymentDataAccessService.savePayment(paymentRequest);
        reserveBalance(payment);

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

    private void reserveBalance(PaymentModel payment) {
        walletServiceAdapter.reserveBalance(
                Long.valueOf(payment.getSource()),
                new ReservationRequest(payment.getId(), payment.getAmount()));
    }
}
