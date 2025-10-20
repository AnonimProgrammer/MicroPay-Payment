package com.micropay.payment.service.processor.refund.impl;

import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.service.processor.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CardRefundProcessor implements PaymentProcessor<PaymentModel> {

    @Override
    public void processPayment(PaymentModel paymentObject) {

        log.info("Processing refund.");

    }
}
