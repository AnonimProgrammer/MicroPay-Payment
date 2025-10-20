package com.micropay.payment.dto.wallet.refund;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletRefundedEvent (
        UUID eventId,
        Long paymentId,
        Long walletId,
        BigDecimal amount
) {}