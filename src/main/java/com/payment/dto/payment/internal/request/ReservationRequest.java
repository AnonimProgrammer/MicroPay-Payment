package com.payment.dto.payment.internal.request;

import java.math.BigDecimal;

public class ReservationRequest {

    private Long paymentId;
    private BigDecimal requestedAmount;

    public ReservationRequest() {}
    public ReservationRequest(Long paymentId, BigDecimal amount) {
        this.paymentId = paymentId;
        this.requestedAmount = amount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
}
