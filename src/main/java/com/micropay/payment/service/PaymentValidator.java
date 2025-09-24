package com.micropay.payment.service;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.exception.InvalidPaymentRequestException;
import com.micropay.payment.model.payment.CardDetails;
import com.micropay.payment.model.transaction.TransactionType;

public class PaymentValidator {

    public static void validateTransactionType(PaymentRequest paymentRequest, TransactionType expectedType) {
        if (paymentRequest.getType().compareTo(expectedType) != 0) {
            throw new InvalidPaymentRequestException("Invalid transaction type for this processor.");
        }
    }

    public static void validateCardNumber(String cardNumber) {
        if (!cardNumber.matches("\\d{16}")) {
            throw new InvalidPaymentRequestException("Invalid card number format.");
        }
    }

    public static void validateWalletId(String walletId) {
        if (!walletId.matches("\\d+")) {
            throw new InvalidPaymentRequestException("Invalid wallet ID format.");
        }
        if (Long.parseLong(walletId) <= 0) {
            throw new InvalidPaymentRequestException("Wallet ID must be greater than 0.");
        }
    }

    public static void validateCardDetails(CardDetails cardDetails) {
        if (cardDetails.getNumber() == null || cardDetails.getExpirationMonth() == null ||
                cardDetails.getExpirationYear() == null || cardDetails.getCvv() == null) {
            throw new InvalidPaymentRequestException("Invalid card details provided.");
        }
        validateCardNumber(cardDetails.getNumber());
        if (!cardDetails.getExpirationMonth().matches("^(0[1-9]|1[0-2])$")) {
            throw new InvalidPaymentRequestException("Invalid expiration format.");
        }

        if (!cardDetails.getExpirationYear().matches("^\\d{2}$")) {
            throw new InvalidPaymentRequestException("Invalid expiration year format.");
        }

        if (!cardDetails.getCvv().matches("^\\d{3}$")) {
            throw new InvalidPaymentRequestException("Invalid CVV format.");
        }
    }

}
