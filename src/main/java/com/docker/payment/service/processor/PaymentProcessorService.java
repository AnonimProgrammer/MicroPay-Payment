package com.docker.payment.service.processor;

import com.docker.payment.dto.payment.internal.PaymentRequest;
import com.docker.payment.exception.PaymentProcessorNotFoundException;
import com.docker.payment.model.payment.PaymentKey;

import java.util.Map;

public abstract class PaymentProcessorService {

    public PaymentProcessor getPaymentProcessor(PaymentRequest paymentRequest,
                                                Map<PaymentKey, PaymentProcessor> registry) {
        PaymentKey paymentKey = new PaymentKey(
                paymentRequest.getType(),
                paymentRequest.getSourceType(),
                paymentRequest.getDestinationType()
        );
        PaymentProcessor processor = registry.get(paymentKey);
        if (processor == null) {
            throw new PaymentProcessorNotFoundException("No payment processor for key: " + paymentKey);
        }
        return processor;
    }
}
