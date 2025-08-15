package com.docker.payment.dto.transaction;

import com.docker.payment.model.transaction.EndpointType;
import com.docker.payment.model.transaction.TransactionType;

import java.math.BigDecimal;

public class InitiateTransactionEvent {

    private Long paymentId;
    private BigDecimal amount;
    private String source;
    private EndpointType sourceType;
    private String destination;
    private EndpointType destinationType;
    private TransactionType type;

    public InitiateTransactionEvent() {}

    private InitiateTransactionEvent(Builder builder) {
        this.paymentId = builder.paymentId;
        this.amount = builder.amount;
        this.source = builder.source;
        this.sourceType = builder.sourceType;
        this.destination = builder.destination;
        this.destinationType = builder.destinationType;
        this.type = builder.type;
    }

    public static class Builder {
        private Long paymentId;
        private BigDecimal amount;
        private String source;
        private EndpointType sourceType;
        private String destination;
        private EndpointType destinationType;
        private TransactionType type;

        public Builder paymentId(Long paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
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

        public InitiateTransactionEvent build() {
            return new InitiateTransactionEvent(this);
        }
    }

    // Getters and setters
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public EndpointType getSourceType() {
        return sourceType;
    }

    public void setSourceType(EndpointType sourceType) {
        this.sourceType = sourceType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public EndpointType getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(EndpointType destinationType) {
        this.destinationType = destinationType;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "InitiateTransactionEvent{" +
                "paymentId=" + paymentId +
                ", amount=" + amount +
                ", source='" + source + '\'' +
                ", sourceType=" + sourceType +
                ", destination='" + destination + '\'' +
                ", destinationType=" + destinationType +
                ", type=" + type +
                '}';
    }
}
