package com.payment.model.payment;

import com.payment.model.transaction.EndpointType;
import com.payment.model.transaction.TransactionType;

public record PaymentKey(TransactionType type, EndpointType source, EndpointType destination) {}

