package com.payment.service.processor.top_up;

import com.payment.config.external.BankingConfiguration;
import com.payment.dto.payment.external.request.CardWithdrawalRequest;
import com.payment.dto.payment.external.response.BankApiError;
import com.payment.dto.payment.external.response.BankApiResponse;
import com.payment.dto.payment.internal.request.PaymentRequest;
import com.payment.exception.PaymentProviderException;
import com.payment.model.payment.CardDetails;
import com.payment.model.transaction.TransactionType;
import com.payment.service.PaymentValidator;
import com.payment.service.external.CardServiceClient;
import com.payment.service.processor.PaymentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CardToWalletProcessor implements PaymentProcessor {

    private final CardServiceClient walletCardClient;
    private final static Logger logger = LoggerFactory.getLogger(CardToWalletProcessor.class);

    public CardToWalletProcessor(CardServiceClient walletCardClient) {
        this.walletCardClient = walletCardClient;
    }

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
