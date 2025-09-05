package com.payment.service.processor.withdrawal;

import com.payment.config.external.BankingConfiguration;
import com.payment.dto.payment.external.response.BankApiError;
import com.payment.dto.payment.external.response.BankApiResponse;
import com.payment.dto.payment.external.request.CardTopUpRequest;
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
public class WalletToCardProcessor implements PaymentProcessor {

    private final CardServiceClient walletCardClient;
    private final static Logger logger = LoggerFactory.getLogger(WalletToCardProcessor.class);

    public WalletToCardProcessor(CardServiceClient walletCardClient) {
        this.walletCardClient = walletCardClient;
    }

    @Override
    public void processPayment(PaymentRequest paymentRequest) {
        logger.info("[WalletToCardProcessor] - Processing Payment Request.");

        CardDetails cardDetails = (CardDetails) paymentRequest.getPaymentDetails();
        validatePaymentRequest(paymentRequest, cardDetails);

        logger.info("[WalletToCardProcessor] - Payment Request validation completed successfully.");

        CardTopUpRequest cardTopUpRequest = new CardTopUpRequest.Builder()
                .setMerchantId(BankingConfiguration.getMerchantId())
                .setAmount(paymentRequest.getAmount())
                .setCurrency("AZN")
                .setTransactionType(TransactionType.TOP_UP)
                .setRecipient(new CardTopUpRequest.Recipient(cardDetails.getNumber()))
                .setDescription("Card top-up from MicroPay-Wallet.")
                .build();

        BankApiResponse bankApiResponse = walletCardClient.topUpCard(cardTopUpRequest);
        logger.info("[WalletToCardProcessor] - Bank API response: {}", bankApiResponse);

        BankApiError error = bankApiResponse.getError();
        if (bankApiResponse.getStatus().equals("FAILED") && error != null) {
            throw new PaymentProviderException(error.getErrorMessage(), error.getErrorCode());
        }
    }

    private void validatePaymentRequest(PaymentRequest request, CardDetails cardDetails) {
        PaymentValidator.validateCardNumber(request.getDestination());
        PaymentValidator.validateWalletId(request.getSource());

        PaymentValidator.validateCardNumber(cardDetails.getNumber());
    }
}
