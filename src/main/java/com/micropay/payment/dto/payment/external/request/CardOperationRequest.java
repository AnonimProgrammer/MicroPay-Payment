package com.micropay.payment.dto.payment.external.request;

import com.micropay.payment.model.payment.external.Metadata;
import com.micropay.payment.model.payment.external.Recipient;
import com.micropay.payment.model.transaction.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CardOperationRequest {

    private String merchantId;
    private BigDecimal amount;
    private String currency;
    private TransactionType transactionType;
    private UUID transactionId;
    private Recipient recipient;
    private String description;
    private LocalDateTime timestamp;
    private Metadata metadata;

    private CardOperationRequest(Builder builder) {
        this.merchantId = builder.merchantId;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.transactionType = builder.transactionType;
        this.transactionId = builder.transactionId;
        this.recipient = builder.recipient;
        this.description = builder.description;
        this.timestamp = builder.timestamp != null ? builder.timestamp : LocalDateTime.now();
        this.metadata = builder.metadata;
    }

    public static class Builder {
        private String merchantId;
        private BigDecimal amount;
        private String currency;
        private TransactionType transactionType;
        private UUID transactionId;
        private Recipient recipient;
        private String description;
        private LocalDateTime timestamp;
        private Metadata metadata;

        public Builder setMerchantId(String merchantId) {
            this.merchantId = merchantId;
            return this;
        }

        public Builder setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder setTransactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder setTransactionId(UUID transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder setRecipient(Recipient recipient) {
            this.recipient = recipient;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setMetadata(Metadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public CardOperationRequest build() {
            return new CardOperationRequest(this);
        }
    }

}

