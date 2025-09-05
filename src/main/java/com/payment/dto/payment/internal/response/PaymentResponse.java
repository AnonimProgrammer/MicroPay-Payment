package com.payment.dto.payment.internal.response;

import com.payment.model.payment.PaymentStatus;
import com.payment.model.transaction.EndpointType;
import com.payment.model.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    private Long id;
    private BigDecimal amount;
    private PaymentStatus status;
    private String source;
    private EndpointType sourceType;
    private String destination;
    private EndpointType destinationType;
    private TransactionType type;
    private LocalDateTime createdAt;

    public PaymentResponse () {}
    public PaymentResponse(Builder builder) {
        this.id = builder.paymentId;
        this.amount = builder.amount;
        this.status = builder.status;
        this.source = builder.source;
        this.sourceType = builder.sourceType;
        this.destination = builder.destination;
        this.destinationType = builder.destinationType;
        this.type = builder.type;
        this.createdAt = builder.createdAt;
    }

    public static class Builder {
        private Long paymentId;
        private BigDecimal amount;
        private PaymentStatus status;
        private String source;
        private EndpointType sourceType;
        private String destination;
        private EndpointType destinationType;
        private TransactionType type;
        private LocalDateTime createdAt;

        public Builder paymentId(Long paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder status(PaymentStatus status) {
            this.status = status;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder sourceType(EndpointType sourceType) {
            this.sourceType = sourceType;
            return this;
        }

        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder destinationType(EndpointType destinationType) {
            this.destinationType = destinationType;
            return this;
        }

        public Builder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PaymentResponse build() {
            return new PaymentResponse(this);
        }

    }

    // Getters
    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public EndpointType getSourceType() {
        return sourceType;
    }

    public String getDestination() {
        return destination;
    }

    public EndpointType getDestinationType() {
        return destinationType;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setSourceType(EndpointType sourceType) {
        this.sourceType = sourceType;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDestinationType(EndpointType destinationType) {
        this.destinationType = destinationType;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

