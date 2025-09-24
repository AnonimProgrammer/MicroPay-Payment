package com.micropay.payment.dto.payment.internal.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReservationErrorResponse extends ErrorResponse {

    private BigDecimal availableBalance;
    private BigDecimal requestedAmount;

    public ReservationErrorResponse(Integer errorCode, String message, LocalDateTime timestamp, BigDecimal availableBalance, BigDecimal requestedAmount) {
        super(errorCode, message, timestamp);
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }

}
