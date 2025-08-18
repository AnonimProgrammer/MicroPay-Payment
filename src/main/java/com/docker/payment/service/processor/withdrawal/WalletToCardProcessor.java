package com.docker.payment.service.processor.withdrawal;

import com.docker.payment.config.external.BankingConfiguration;
import com.docker.payment.dto.payment.external.BankApiError;
import com.docker.payment.dto.payment.external.BankApiResponse;
import com.docker.payment.dto.payment.external.CardTopUpRequest;
import com.docker.payment.dto.payment.internal.PaymentRequest;
import com.docker.payment.exception.PaymentProviderException;
import com.docker.payment.model.payment.CardDetails;
import com.docker.payment.model.transaction.TransactionType;
import com.docker.payment.service.PaymentValidator;
import com.docker.payment.service.external.WalletCardClient;
import com.docker.payment.service.processor.PaymentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WalletToCardProcessor implements PaymentProcessor {

    private final WalletCardClient walletCardClient;
    private final static Logger logger = LoggerFactory.getLogger(WalletToCardProcessor.class);

    public WalletToCardProcessor(WalletCardClient walletCardClient) {
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
        PaymentValidator.validateCardNumber(request.getSource());
        PaymentValidator.validateWalletId(request.getDestination());

        PaymentValidator.validateCardNumber(cardDetails.getNumber());
    }
}
