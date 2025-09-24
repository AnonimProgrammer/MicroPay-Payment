package com.micropay.payment.model.payment;

import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
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

}
