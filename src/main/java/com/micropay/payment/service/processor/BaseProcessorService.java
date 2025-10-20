package com.micropay.payment.service.processor;

import com.micropay.payment.exception.PaymentProcessorNotFoundException;
import com.micropay.payment.model.payment.PaymentKey;

import java.util.Map;

public interface BaseProcessorService {

     default PaymentProcessor getPaymentProcessor(
            PaymentKey key, Map<PaymentKey, PaymentProcessor> registry
    ) {
        PaymentProcessor processor = registry.get(key);
        if (processor == null) {
            throw new PaymentProcessorNotFoundException("No payment processor for key: " + key);
        }
        return processor;
    }

}
