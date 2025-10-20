package com.micropay.payment.dto.transaction;

import java.util.UUID;

public record TransactionCreatedEvent(
        UUID eventId,
        Long paymentId,
        UUID transactionId
) {}