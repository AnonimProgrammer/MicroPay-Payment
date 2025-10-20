package com.micropay.payment.model.payment.external;

import lombok.Data;

@Data
public class Recipient {
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
