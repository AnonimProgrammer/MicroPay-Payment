package com.payment.service.processor;

import com.payment.dto.payment.internal.request.PaymentRequest;

public interface PaymentProcessor {

    void processPayment(PaymentRequest paymentRequest);
}

