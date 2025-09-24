package com.micropay.payment.dto.transaction;

import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InitiateTransactionEvent {

    private Long paymentId;
    private BigDecimal amount;
    private String source;
    private EndpointType sourceType;
    private String destination;
    private EndpointType destinationType;
    private TransactionType type;

    public InitiateTransactionEvent(Builder builder) {
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
