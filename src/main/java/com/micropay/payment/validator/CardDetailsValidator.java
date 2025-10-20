package com.micropay.payment.validator;

import com.micropay.payment.exception.InvalidPaymentRequestException;
import com.micropay.payment.model.payment.CardDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public abstract class CardDetailsValidator {

    public void validateCardNumber(String cardNumber) {
        cardNumber = cardNumber.replaceAll("[\\s-]", "");
        if (!cardNumber.matches("\\d{16}")) {
            throw new InvalidPaymentRequestException("Invalid card number format.");
        }
    }

    public void validateCardDetails(CardDetails cardDetails) {
        if (cardDetails == null) {
            throw new InvalidPaymentRequestException("Card details must not be null.");
        }
        String expMonth = cardDetails.getExpirationMonth();
        String expYear = cardDetails.getExpirationYear();
        String cvv = cardDetails.getCvv();

        if (expMonth == null || expYear == null || cvv == null) {
            throw new InvalidPaymentRequestException("All card fields (number, expiration month/year, CVV) must be provided.");
        }

        if (!expMonth.matches("^(0[1-9]|1[0-2])$")) {
            throw new InvalidPaymentRequestException("Invalid expiration month format. Expected 01–12.");
        }

        if (!expYear.matches("^\\d{2}$")) {
            throw new InvalidPaymentRequestException("Invalid expiration year format. Expected YY.");
        }

        expYear = "20" + expYear;
        try {
            int month = Integer.parseInt(expMonth);
            int year = Integer.parseInt(expYear);

            YearMonth expiry = YearMonth.of(year, month);
            if (expiry.isBefore(YearMonth.now())) {
                throw new InvalidPaymentRequestException("Card is expired.");
            }
        } catch (DateTimeException exception) {
            throw new InvalidPaymentRequestException("Invalid expiration date.");
        }

        if (!cvv.matches("^\\d{3}$")) {
            throw new InvalidPaymentRequestException("Invalid CVV format. Expected 3 digits.");
        }
    }

}
