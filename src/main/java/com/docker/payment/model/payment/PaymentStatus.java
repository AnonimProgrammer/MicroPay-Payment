package com.docker.payment.model.payment;

public enum PaymentStatus {
    PENDING,
    DEBIT_FAILED,
    CREDIT_FAILED,
    COMPLETED
}
