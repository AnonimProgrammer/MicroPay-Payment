package com.payment.model.payment;

public enum PaymentStatus {
    PENDING,        // Waiting for transaction confirmation
    PROCESSING,     // Actively being processed

    DEBITED,       // Funds successfully debited
    CREDITED,      // Funds successfully credited
    REFUNDED,      // Compensation/refund applied

    COMPLETED,      // Successful
    FAILED,         // Generic failure
    DEBIT_FAILED,   // Specific failure: debit step
    CREDIT_FAILED,  // Specific failure: credit step
}
