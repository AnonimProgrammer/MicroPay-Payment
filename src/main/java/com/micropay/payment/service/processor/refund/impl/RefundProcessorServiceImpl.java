package com.micropay.payment.service.processor.refund.impl;

import com.micropay.payment.model.payment.PaymentKey;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.processor.PaymentProcessor;
import com.micropay.payment.service.processor.refund.RefundProcessorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefundProcessorServiceImpl implements RefundProcessorService {

    private final Map<PaymentKey, PaymentProcessor> registry = new HashMap<>();

    private final CardRefundProcessor cardRefundProcessor;

    @PostConstruct
    void initializeRegistry() {
        registry.put(new PaymentKey(TransactionType.TOP_UP, EndpointType.CARD, EndpointType.WALLET), cardRefundProcessor);
    }

    @Override
    public void processRefund(PaymentModel payment) {
        PaymentKey key = new PaymentKey(
                payment.getType(),
                payment.getSourceType(),
                payment.getDestinationType()
        );

        PaymentProcessor paymentProcessor = getPaymentProcessor(key, registry);
        paymentProcessor.processPayment(payment);
    }

}
