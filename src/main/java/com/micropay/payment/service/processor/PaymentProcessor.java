package com.micropay.payment.service.processor;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;

public interface PaymentProcessor {

    void processPayment(PaymentRequest paymentRequest);
}

