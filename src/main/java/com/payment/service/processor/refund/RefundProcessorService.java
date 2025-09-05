package com.payment.service.processor.refund;

import com.payment.dto.payment.internal.request.PaymentRequest;
import com.payment.factory.PaymentDtoFactory;
import com.payment.model.payment.PaymentKey;
import com.payment.model.payment.PaymentModel;
import com.payment.model.transaction.EndpointType;
import com.payment.model.transaction.TransactionType;
import com.payment.service.processor.BaseProcessorService;
import com.payment.service.processor.PaymentProcessor;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RefundProcessorService extends BaseProcessorService {

    private final Map<PaymentKey, PaymentProcessor> registry = new HashMap<>();

    private final CardRefundProcessor cardRefundProcessor;
    private final PaymentDtoFactory paymentDtoFactory;

    public RefundProcessorService(
            CardRefundProcessor cardRefundProcessor, PaymentDtoFactory paymentDtoFactory
    ) {
        this.cardRefundProcessor = cardRefundProcessor;
        this.paymentDtoFactory = paymentDtoFactory;
    }

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
