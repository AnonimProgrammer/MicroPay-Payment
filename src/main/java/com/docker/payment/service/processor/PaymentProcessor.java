package com.docker.payment.service.processor;

import com.docker.payment.dto.payment.internal.request.PaymentRequest;

public interface PaymentProcessor {

    void processPayment(PaymentRequest paymentRequest);
}

