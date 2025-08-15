package com.docker.payment.model.payment;

import com.docker.payment.model.transaction.EndpointType;
import com.docker.payment.model.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentModel {

    private Long id;
    private UUID transactionId;
    private BigDecimal amount;
    private PaymentStatus status;
    private String source;
    private EndpointType sourceType;
    private String destination;
    private EndpointType destinationType;
    private TransactionType type;
    private String failureReason;
    private boolean debitCompleted;
    private boolean creditCompleted;
    private boolean refundCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PaymentModel() {
    }
    public PaymentModel(Builder builder) {
        this.transactionId = builder.transactionId;
        this.amount = builder.amount;
        this.status = builder.status;
        this.source = builder.source;
        this.sourceType = builder.sourceType;
        this.destination = builder.destination;
        this.destinationType = builder.destinationType;
        this.type = builder.type;
        this.failureReason = builder.failureReason;
        this.debitCompleted = builder.debitCompleted;
        this.creditCompleted = builder.creditCompleted;
        this.refundCompleted = builder.refundCompleted;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static class Builder {
        private UUID transactionId;
        private BigDecimal amount;
        private PaymentStatus status;
        private String source;
        private EndpointType sourceType;
        private String destination;
        private EndpointType destinationType;
        private TransactionType type;
        private String failureReason;
        private boolean debitCompleted;
        private boolean creditCompleted;
        private boolean refundCompleted;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder transactionId(UUID transactionId) {
            this.transactionId = transactionId;
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

        public Builder failureReason(String failureReason) {
            this.failureReason = failureReason;
            return this;
        }

        public Builder debitCompleted(boolean debitCompleted) {
            this.debitCompleted = debitCompleted;
            return this;
        }

        public Builder creditCompleted(boolean creditCompleted) {
            this.creditCompleted = creditCompleted;
            return this;
        }

        public Builder refundCompleted(boolean refundCompleted) {
            this.refundCompleted = refundCompleted;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PaymentModel build() {
            return new PaymentModel(this);
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public UUID getTransactionId() {
        return transactionId;
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

    public String getFailureReason() {
        return failureReason;
    }

    public boolean isDebitCompleted() {
        return debitCompleted;
    }

    public boolean isCreditCompleted() {
        return creditCompleted;
    }

    public boolean isRefundCompleted() {
        return refundCompleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
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

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public void setDebitCompleted(boolean debitCompleted) {
        this.debitCompleted = debitCompleted;
    }

    public void setCreditCompleted(boolean creditCompleted) {
        this.creditCompleted = creditCompleted;
    }

    public void setRefundCompleted(boolean refundCompleted) {
        this.refundCompleted = refundCompleted;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
