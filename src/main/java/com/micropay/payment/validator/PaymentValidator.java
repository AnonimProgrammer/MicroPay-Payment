package com.micropay.payment.validator;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.exception.InvalidPaymentRequestException;
import com.micropay.payment.model.transaction.TransactionType;
import org.springframework.stereotype.Component;

@Component
public class PaymentValidator extends CardDetailsValidator {

    public void validateTransactionType(PaymentRequest paymentRequest, TransactionType expectedType) {
        if (paymentRequest.getType().compareTo(expectedType) != 0) {
            throw new InvalidPaymentRequestException("Invalid transaction type for this processor.");
        }
    }

    public void validateWalletId(String walletId) {
        if (!walletId.matches("\\d+")) {
            throw new InvalidPaymentRequestException("Invalid wallet ID format.");
        }
        if (Long.parseLong(walletId) <= 0) {
            throw new InvalidPaymentRequestException("Wallet ID must be greater than 0.");
        }
    }

}
