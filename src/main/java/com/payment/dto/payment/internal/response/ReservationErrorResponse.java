package com.payment.dto.payment.internal.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservationErrorResponse extends ErrorResponse {

    private BigDecimal availableBalance;
    private BigDecimal requestedAmount;

    public ReservationErrorResponse() {}
    public ReservationErrorResponse(Integer errorCode, String message, LocalDateTime timestamp, BigDecimal availableBalance, BigDecimal requestedAmount) {
        super(errorCode, message, timestamp);
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }
    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }
}
