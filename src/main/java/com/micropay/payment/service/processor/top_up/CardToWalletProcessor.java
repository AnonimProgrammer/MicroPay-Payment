package com.micropay.payment.service.processor.top_up;

import com.micropay.payment.config.external.BankingConfiguration;
import com.micropay.payment.dto.payment.external.request.CardWithdrawalRequest;
import com.micropay.payment.dto.payment.external.response.BankApiError;
import com.micropay.payment.dto.payment.external.response.BankApiResponse;
import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.exception.PaymentProviderException;
import com.micropay.payment.model.payment.CardDetails;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.PaymentValidator;
import com.micropay.payment.service.external.CardServiceClient;
import com.micropay.payment.service.processor.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardToWalletProcessor implements PaymentProcessor {

    private final CardServiceClient walletCardClient;
    private final static Logger logger = LoggerFactory.getLogger(CardToWalletProcessor.class);

    @Override
    public void processPayment(PaymentRequest paymentRequest) {
        logger.info("[CardToWalletProcessor] - Processing Payment Request.");

        CardDetails cardDetails = (CardDetails) paymentRequest.getPaymentDetails();
        validatePaymentRequest(paymentRequest, cardDetails);

        logger.info("[CardToWalletProcessor] - Payment Request validation completed successfully.");

        CardWithdrawalRequest cardWithdrawalRequest = new CardWithdrawalRequest.Builder()
                .setMerchantId(BankingConfiguration.getMerchantId())
                .setAmount(paymentRequest.getAmount())
                .setCurrency("AZN")
                .setTransactionType(TransactionType.WITHDRAWAL)
                .setRecipient(new CardWithdrawalRequest.Recipient.RecipientBuilder()
                        .setCardNumber(cardDetails.getNumber())
                        .setCardExpiryMonth(cardDetails.getExpirationMonth())
                        .setCardExpiryYear(cardDetails.getExpirationYear())
                        .setCvv(cardDetails.getCvv())
                        .build())
                .setDescription("Card withdrawal to MicroPay-Wallet.")
                .build();

        BankApiResponse bankApiResponse = walletCardClient.withdrawCard(cardWithdrawalRequest);
        logger.info("[CardToWalletProcessor] - Bank API response: {}", bankApiResponse);

        BankApiError error = bankApiResponse.getError();
        if (bankApiResponse.getStatus().equals("FAILED") && error != null) {
            throw new PaymentProviderException(error.getErrorMessage(), error.getErrorCode());
        }
    }

    private void validatePaymentRequest(PaymentRequest request, CardDetails cardDetails) {
        PaymentValidator.validateCardNumber(request.getSource());
        PaymentValidator.validateWalletId(request.getDestination());

        PaymentValidator.validateCardDetails(cardDetails);
    }

}
