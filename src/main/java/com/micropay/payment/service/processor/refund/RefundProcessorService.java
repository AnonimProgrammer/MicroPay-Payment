package com.micropay.payment.service.processor.refund;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.factory.PaymentDtoFactory;
import com.micropay.payment.model.payment.PaymentKey;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.processor.BaseProcessorService;
import com.micropay.payment.service.processor.PaymentProcessor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefundProcessorService extends BaseProcessorService {

    private final Map<PaymentKey, PaymentProcessor> registry = new HashMap<>();

    private final CardRefundProcessor cardRefundProcessor;
    private final PaymentDtoFactory paymentDtoFactory;

    @PostConstruct
    private void initializeRegistry() {
        registry.put(new PaymentKey(TransactionType.TOP_UP, EndpointType.CARD, EndpointType.WALLET), cardRefundProcessor);
    }

    public PaymentProcessor getPaymentProcessor(PaymentRequest paymentRequest) {
        return super.getPaymentProcessor(paymentRequest, registry);
    }

    public void processRefund(PaymentModel payment){
        PaymentRequest paymentRequest = paymentDtoFactory.createPaymentRequest(payment, null);

        PaymentProcessor paymentProcessor = getPaymentProcessor(paymentRequest);
        paymentProcessor.processPayment(paymentRequest);
    }

}
