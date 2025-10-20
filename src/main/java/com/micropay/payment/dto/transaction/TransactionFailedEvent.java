package com.micropay.payment.dto.transaction;

import java.util.UUID;

public record TransactionFailedEvent(
        UUID eventId,
        UUID transactionId,
        String failureReason
) {}
