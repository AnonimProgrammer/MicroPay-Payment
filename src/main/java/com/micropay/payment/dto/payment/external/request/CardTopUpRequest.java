package com.micropay.payment.dto.payment.external.request;

import com.micropay.payment.model.transaction.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CardTopUpRequest {

    private String merchantId;
    private BigDecimal amount;
    private String currency;
    private TransactionType transactionType;
    private Recipient recipient;
    private String description;
    private LocalDateTime timestamp;

    private CardTopUpRequest(Builder builder) {
        this.merchantId = builder.merchantId;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.transactionType = builder.transactionType;
        this.recipient = builder.recipient;
        this.description = builder.description;
        this.timestamp = builder.timestamp != null ? builder.timestamp : LocalDateTime.now();
    }

    public static class Recipient {
        private String cardNumber;

        public Recipient(String cardNumber) {
            this.cardNumber = cardNumber;
        }
    }

    public static class Builder {
        private String merchantId;
        private BigDecimal amount;
        private String currency;
        private TransactionType transactionType;
        private Recipient recipient;
        private String description;
        private LocalDateTime timestamp;

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

        public CardTopUpRequest build() {
            return new CardTopUpRequest(this);
        }
    }

}


