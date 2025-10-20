package com.micropay.payment.service.processor.top_up.impl;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.mapper.PaymentMapper;
import com.micropay.payment.mapper.TransactionEventMapper;
import com.micropay.payment.service.messaging.transaction.TransactionMessageDispatcher;
import com.micropay.payment.model.payment.PaymentKey;
import com.micropay.payment.model.payment.entity.Payment;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.payment.PaymentDataAccessService;
import com.micropay.payment.service.processor.top_up.TopUpProcessorService;
import com.micropay.payment.validator.PaymentValidator;
import com.micropay.payment.service.processor.PaymentProcessor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopUpProcessorServiceImpl implements TopUpProcessorService {

    private final Map<PaymentKey, PaymentProcessor> registry = new HashMap<>();

    private final CardToWalletProcessor cardToWalletProcessor;
    private final PaymentDataAccessService paymentDataAccessService;
    private final TransactionMessageDispatcher transactionMessageDispatcher;
    private final TransactionEventMapper transactionEventMapper;
    private final PaymentMapper paymentMapper;
    private final PaymentValidator paymentValidator;

    @PostConstruct
    void initializeRegistry() {
        registry.put(new PaymentKey(TransactionType.TOP_UP, EndpointType.CARD, EndpointType.WALLET), cardToWalletProcessor);
    }

    @Override
    public PaymentResponse processPaymentRequest(PaymentRequest paymentRequest) {
        log.info("Processing payment request.");

        paymentValidator.validateTransactionType(paymentRequest, TransactionType.TOP_UP);
        paymentValidator.validateWalletId(paymentRequest.getDestination());

        processTopUp(paymentRequest);
        Payment payment = paymentDataAccessService.savePayment(paymentRequest);

        InitiateTransactionEvent initiateTransactionEvent = transactionEventMapper.mapToInitiateTransactionEvent(payment);
        transactionMessageDispatcher.publishInitiateTransactionEvent(initiateTransactionEvent);

        return paymentMapper.toResponse(payment);
    }

    private void processTopUp(PaymentRequest request){
        PaymentKey key = new PaymentKey(
                request.getType(),
                request.getSourceType(),
                request.getDestinationType()
        );
        PaymentProcessor paymentProcessor = getPaymentProcessor(key, registry);
        paymentProcessor.processPayment(request);
    }

}
