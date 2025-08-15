package com.docker.payment.service.processor;

import com.docker.payment.dto.payment.internal.PaymentRequest;

public interface PaymentProcessor {

    void processPayment(PaymentRequest paymentRequest);
}

