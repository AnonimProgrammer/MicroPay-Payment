package com.micropay.payment.dto.wallet.credit;

import java.math.BigDecimal;
import java.util.UUID;

public record CreditWalletEvent (
        UUID eventId,
        Long paymentId,
        Long walletId,
        BigDecimal amount
) {}