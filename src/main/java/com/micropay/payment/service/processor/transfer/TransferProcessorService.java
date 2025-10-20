package com.micropay.payment.service.processor.transfer;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;

public interface TransferProcessorService {

    PaymentResponse processPaymentRequest(PaymentRequest paymentRequest);
}
