package com.docker.payment.dto.payment.internal;

import com.docker.payment.model.payment.PaymentDetails;
import com.docker.payment.model.transaction.EndpointType;
import com.docker.payment.model.transaction.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class PaymentRequest {

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    private BigDecimal amount;

    @NotBlank(message = "Source must not be blank")
    private String source;

    @NotNull(message = "Source type must not be null")
    private EndpointType sourceType;

    @NotBlank(message = "Destination must not be blank")
    private String destination;

    @NotNull(message = "Destination type must not be null")
    private EndpointType destinationType;

    @NotNull(message = "Transaction type must not be null")
    private TransactionType type;

    @NotNull(message = "Payment details must not be null")
    private PaymentDetails paymentDetails;

    public PaymentRequest() {}
    public PaymentRequest(BigDecimal amount, String source, EndpointType sourceType,
                          String destination, EndpointType destinationType,
                          TransactionType type, PaymentDetails paymentDetails) {
        this.amount = amount;
        this.source = source;
        this.sourceType = sourceType;
        this.destination = destination;
        this.destinationType = destinationType;
        this.type = type;
        this.paymentDetails = paymentDetails;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public EndpointType getSourceType() {
        return sourceType;
    }

    public void setSourceType(EndpointType sourceType) {
        this.sourceType = sourceType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public EndpointType getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(EndpointType destinationType) {
        this.destinationType = destinationType;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}

