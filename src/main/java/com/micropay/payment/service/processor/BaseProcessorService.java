package com.micropay.payment.service.processor;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.exception.PaymentProcessorNotFoundException;
import com.micropay.payment.model.payment.PaymentKey;

import java.util.Map;

public abstract class BaseProcessorService {

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
