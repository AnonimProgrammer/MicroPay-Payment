package com.micropay.payment.service.processor.withdrawal;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.service.processor.BaseProcessorService;

public interface WithdrawalProcessorService extends BaseProcessorService {

    PaymentResponse processPaymentRequest(PaymentRequest paymentRequest);

    void processWithdrawal(PaymentModel payment);
}
