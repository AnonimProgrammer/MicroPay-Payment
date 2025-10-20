package com.micropay.payment.service.processor.top_up;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.service.processor.BaseProcessorService;

public interface TopUpProcessorService extends BaseProcessorService {

    PaymentResponse processPaymentRequest(PaymentRequest paymentRequest);

}
