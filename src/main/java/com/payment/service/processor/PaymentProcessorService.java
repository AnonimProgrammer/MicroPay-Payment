package com.payment.service.processor;

import com.payment.dto.payment.internal.request.PaymentRequest;
import com.payment.dto.payment.internal.response.PaymentResponse;

public interface PaymentProcessorService {

    PaymentResponse processPaymentRequest(PaymentRequest paymentRequest);
}
