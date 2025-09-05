package com.payment.service.processor.top_up;

import com.payment.dto.payment.internal.request.PaymentRequest;
import com.payment.dto.payment.internal.response.PaymentResponse;
import com.payment.dto.transaction.InitiateTransactionEvent;
import com.payment.factory.PaymentDtoFactory;
import com.payment.factory.TransactionEventFactory;
import com.payment.messaging.transaction.TransactionMessagePublisher;
import com.payment.model.payment.PaymentKey;
import com.payment.model.payment.PaymentModel;
import com.payment.model.transaction.EndpointType;
import com.payment.model.transaction.TransactionType;
import com.payment.service.PaymentDataAccessService;
import com.payment.service.PaymentValidator;
import com.payment.service.processor.BaseProcessorService;
import com.payment.service.processor.PaymentProcessor;
import com.payment.service.processor.PaymentProcessorService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TopUpProcessorService extends BaseProcessorService implements PaymentProcessorService {

    private final Map<PaymentKey, PaymentProcessor> registry = new HashMap<>();

    private final CardToWalletProcessor cardToWalletProcessor;
    private final PaymentDataAccessService paymentDataAccessService;
    private final TransactionMessagePublisher transactionMessagePublisher;
    private final TransactionEventFactory transactionEventFactory;
    private final PaymentDtoFactory paymentDtoFactory;

    public TopUpProcessorService(
            CardToWalletProcessor cardToWalletProcessor,
            PaymentDataAccessService paymentDataAccessService,
            TransactionMessagePublisher transactionMessagePublisher,
            TransactionEventFactory transactionEventFactory,
            PaymentDtoFactory paymentDtoFactory
    ) {
        this.cardToWalletProcessor = cardToWalletProcessor;
        this.paymentDataAccessService = paymentDataAccessService;
        this.transactionMessagePublisher = transactionMessagePublisher;
        this.transactionEventFactory = transactionEventFactory;
        this.paymentDtoFactory = paymentDtoFactory;
    }

    @PostConstruct
    private void initializeRegistry() {
        registry.put(new PaymentKey(TransactionType.TOP_UP, EndpointType.CARD, EndpointType.WALLET), cardToWalletProcessor);
    }

    @Override
    public PaymentResponse processPaymentRequest(PaymentRequest paymentRequest) {
        PaymentValidator.validateTransactionType(paymentRequest, TransactionType.TOP_UP);
        PaymentValidator.validateWalletId(paymentRequest.getDestination());

        PaymentProcessor paymentProcessor = getPaymentProcessor(paymentRequest, registry);
        paymentProcessor.processPayment(paymentRequest);

        PaymentModel payment = paymentDataAccessService.savePayment(paymentRequest);

        InitiateTransactionEvent initiateTransactionEvent = transactionEventFactory.createInitiateTransactionEvent(payment);
        transactionMessagePublisher.publishInitiateTransactionEvent(initiateTransactionEvent);

        return paymentDtoFactory.createPaymentResponse(payment);
    }

}
