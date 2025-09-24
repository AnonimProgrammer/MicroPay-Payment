package com.micropay.payment.dto.payment.external.request;

import com.micropay.payment.model.transaction.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CardWithdrawalRequest {

    private String merchantId;
    private BigDecimal amount;
    private String currency;
    private TransactionType transactionType;
    private Recipient recipient;
    private String description;
    private LocalDateTime timestamp;

    private CardWithdrawalRequest(Builder builder) {
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
        private String cardExpiryMonth;
        private String cardExpiryYear;
        private String cvv;

        private Recipient(RecipientBuilder builder) {
            this.cardNumber = builder.cardNumber;
            this.cardExpiryMonth = builder.cardExpiryMonth;
            this.cardExpiryYear = builder.cardExpiryYear;
            this.cvv = builder.cvv;
        }

        public static class RecipientBuilder {
            private String cardNumber;
            private String cardExpiryMonth;
            private String cardExpiryYear;
            private String cvv;

            public RecipientBuilder setCardNumber(String cardNumber) {
                this.cardNumber = cardNumber;
                return this;
            }

            public RecipientBuilder setCardExpiryMonth(String cardExpiryMonth) {
                this.cardExpiryMonth = cardExpiryMonth;
                return this;
            }

            public RecipientBuilder setCardExpiryYear(String cardExpiryYear) {
                this.cardExpiryYear = cardExpiryYear;
                return this;
            }

            public RecipientBuilder setCvv(String cvv) {
                this.cvv = cvv;
                return this;
            }

            public Recipient build() {
                return new Recipient(this);
            }
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

        public CardWithdrawalRequest build() {
            return new CardWithdrawalRequest(this);
        }
    }

}

