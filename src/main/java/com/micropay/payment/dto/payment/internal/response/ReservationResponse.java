package com.micropay.payment.dto.payment.internal.response;

import com.micropay.payment.model.wallet.ReservationStatus;
import java.math.BigDecimal;

public record ReservationResponse(
        Long walletId,
        Long paymentId,
        BigDecimal reservedAmount,
        BigDecimal availableBalance,
        ReservationStatus status
) {}
