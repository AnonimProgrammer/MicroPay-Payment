package com.micropay.payment.dto.wallet.debit;

import java.util.UUID;

public record WalletDebitFailedEvent (
        UUID eventId,
        Long paymentId,
        Long walletId,
        String failureReason
) {}

