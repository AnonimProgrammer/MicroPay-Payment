package com.docker.payment.service.processor.withdrawal;

import com.docker.payment.dto.payment.internal.PaymentRequest;
import com.docker.payment.model.payment.PaymentKey;
import com.docker.payment.model.transaction.EndpointType;
import com.docker.payment.model.transaction.TransactionType;
import com.docker.payment.service.processor.PaymentProcessor;
import com.docker.payment.service.processor.PaymentProcessorService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WithdrawalProcessorService extends PaymentProcessorService {

    private final Map<PaymentKey, PaymentProcessor> registry = new HashMap<>();

    public WithdrawalProcessorService(WalletToCardProcessor walletToCardProcessor) {
        registry.put(new PaymentKey(TransactionType.WITHDRAWAL, EndpointType.WALLET, EndpointType.CARD), walletToCardProcessor);
    }

    public PaymentProcessor getPaymentProcessor(PaymentRequest paymentRequest) {
        return super.getPaymentProcessor(paymentRequest, registry);
    }

}
