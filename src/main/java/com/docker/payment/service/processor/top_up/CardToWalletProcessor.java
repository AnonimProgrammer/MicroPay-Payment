package com.docker.payment.service.processor.top_up;

import com.docker.payment.config.external.BankingConfiguration;
import com.docker.payment.dto.payment.external.BankApiError;
import com.docker.payment.dto.payment.external.BankApiResponse;
import com.docker.payment.dto.payment.internal.PaymentRequest;
import com.docker.payment.dto.payment.external.CardWithdrawalRequest;
import com.docker.payment.exception.InvalidPaymentRequestException;
import com.docker.payment.exception.PaymentProviderException;
import com.docker.payment.model.payment.CardDetails;
import com.docker.payment.model.transaction.TransactionType;
import com.docker.payment.service.external.WalletCardClient;
import com.docker.payment.service.processor.PaymentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CardToWalletProcessor implements PaymentProcessor {

    private final WalletCardClient walletCardClient;
    private final static Logger logger = LoggerFactory.getLogger(CardToWalletProcessor.class);

    public CardToWalletProcessor(WalletCardClient walletCardClient) {
        this.walletCardClient = walletCardClient;
    }

    @Override
    public void processPayment(PaymentRequest paymentRequest) {
        logger.info("[CardToWalletProcessor] - Processing Payment Request.");
        CardDetails cardDetails = (CardDetails) paymentRequest.getPaymentDetails();
        validateCardDetails(cardDetails);
        logger.info("[CardToWalletProcessor] - Card Details validation completed successfully.");

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

    private void validateCardDetails(CardDetails cardDetails) {
        if (cardDetails.getNumber() == null || cardDetails.getExpirationMonth() == null ||
            cardDetails.getExpirationYear() == null || cardDetails.getCvv() == null) {
            logger.error("[CardToWalletProcessor] - Invalid Card Details.");
            throw new InvalidPaymentRequestException("Invalid card details provided.");
        }
    }
}
