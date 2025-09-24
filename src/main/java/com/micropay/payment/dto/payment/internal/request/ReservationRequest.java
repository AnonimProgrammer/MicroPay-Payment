package com.micropay.payment.dto.payment.internal.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {

    private Long paymentId;
    private BigDecimal requestedAmount;

}
