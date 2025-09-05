package com.payment.exception;

public class PaymentProcessorNotFoundException extends RuntimeException {
    public PaymentProcessorNotFoundException(String message) {
        super(message);
    }

    public PaymentProcessorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
