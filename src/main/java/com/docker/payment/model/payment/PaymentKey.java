package com.docker.payment.model.payment;

import com.docker.payment.model.transaction.EndpointType;
import com.docker.payment.model.transaction.TransactionType;

public record PaymentKey(TransactionType type, EndpointType source, EndpointType destination) {}

