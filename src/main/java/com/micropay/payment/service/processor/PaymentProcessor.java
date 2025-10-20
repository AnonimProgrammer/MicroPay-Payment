package com.micropay.payment.service.processor;

public interface PaymentProcessor<T> {

    void processPayment(T paymentObject);
}

