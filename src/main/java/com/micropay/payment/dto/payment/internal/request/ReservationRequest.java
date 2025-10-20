package com.micropay.payment.dto.payment.internal.request;

import java.math.BigDecimal;

public record ReservationRequest(Long paymentId, BigDecimal requestedAmount) {}
