package com.micropay.payment.dto.wallet.debit;

import java.math.BigDecimal;
import java.util.UUID;

public record DebitWalletEvent (
        UUID eventId,
        Long paymentId,
        Long walletId,
        BigDecimal amount
) {}
