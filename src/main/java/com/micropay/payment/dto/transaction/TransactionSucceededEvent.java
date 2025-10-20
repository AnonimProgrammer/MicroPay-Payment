package com.micropay.payment.dto.transaction;

import java.util.UUID;

public record TransactionSucceededEvent(
        UUID eventId,
        UUID transactionId
) {}
