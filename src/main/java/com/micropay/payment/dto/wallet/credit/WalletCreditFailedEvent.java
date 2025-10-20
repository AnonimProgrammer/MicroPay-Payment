package com.micropay.payment.dto.wallet.credit;

import java.util.UUID;

public record WalletCreditFailedEvent(
        UUID eventId,
        Long paymentId,
        Long walletId,
        String failureReason
) {}