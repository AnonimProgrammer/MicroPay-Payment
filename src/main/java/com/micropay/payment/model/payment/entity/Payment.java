package com.micropay.payment.model.payment.entity;

import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.payment.PaymentStatus;
import com.micropay.payment.model.transaction.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID transactionId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false, updatable = false)
    private String source;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private EndpointType sourceType;

    @Column(nullable = false, updatable = false)
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private EndpointType destinationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TransactionType type;

    private String failureReason;

    private boolean debitCompleted;
    private boolean creditCompleted;
    private boolean refundCompleted;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Payment(Builder builder){
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

        public Payment build() {
            return new Payment(this);
        }
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}

