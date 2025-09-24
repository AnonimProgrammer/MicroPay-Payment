package com.micropay.payment.model.payment;

import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;

public record PaymentKey(TransactionType type, EndpointType source, EndpointType destination) {}

