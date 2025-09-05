package com.payment.service.processor.refund;

import com.payment.dto.payment.internal.request.PaymentRequest;
import com.payment.service.processor.PaymentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CardRefundProcessor implements PaymentProcessor {

    private final static Logger logger = LoggerFactory.getLogger(CardRefundProcessor.class);

    @Override
    public void processPayment(PaymentRequest paymentRequest) {

        logger.info("[CardRefundProcessor] - Processing refund.");

    }
}
