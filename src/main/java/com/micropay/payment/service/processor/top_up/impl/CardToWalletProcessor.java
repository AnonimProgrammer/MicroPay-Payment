package com.micropay.payment.service.processor.top_up.impl;

import com.micropay.payment.config.external.BankingConfiguration;
import com.micropay.payment.dto.payment.external.request.CardOperationRequest;
import com.micropay.payment.dto.payment.external.response.BankApiError;
import com.micropay.payment.dto.payment.external.response.BankApiResponse;
import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.exception.PaymentProviderException;
import com.micropay.payment.model.payment.CardDetails;
import com.micropay.payment.model.payment.external.Metadata;
import com.micropay.payment.model.payment.external.Recipient;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.adapter.external.CardServiceAdapter;
import com.micropay.payment.service.processor.PaymentProcessor;
import com.micropay.payment.validator.PaymentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardToWalletProcessor implements PaymentProcessor<PaymentRequest> {

    private final CardServiceAdapter walletCardClient;
    private final PaymentValidator paymentValidator;

    @Override
    public void processPayment(PaymentRequest paymentRequest) {
        log.info("Processing Payment Request.");

        CardDetails cardDetails = (CardDetails) paymentRequest.getPaymentDetails();

        paymentValidator.validateCardNumber(paymentRequest.getSource());
        paymentValidator.validateCardDetails(cardDetails);

        log.info("Payment Request validation completed successfully.");

        CardOperationRequest cardOperationRequest = new CardOperationRequest.Builder()
                .setMerchantId(BankingConfiguration.getMerchantId())
                .setAmount(paymentRequest.getAmount())
                .setCurrency("AZN")
                .setTransactionType(TransactionType.WITHDRAWAL)
                .setTransactionId(UUID.randomUUID())
                .setRecipient(new Recipient.RecipientBuilder()
                        .setCardNumber(paymentRequest.getSource())
                        .setCardExpiryMonth(cardDetails.getExpirationMonth())
                        .setCardExpiryYear(cardDetails.getExpirationYear())
                        .setCvv(cardDetails.getCvv())
                        .build())
                .setDescription("Card withdraw to MicroPay-Wallet.")
                .setMetadata(new Metadata.Builder()
                        .source(EndpointType.WALLET)
                        .operationId(UUID.randomUUID())
                        .initiatedBy("USER")
                        .build())
                .build();

        BankApiResponse bankApiResponse = walletCardClient.withdrawCard(cardOperationRequest);
        log.info("Bank API response: {}", bankApiResponse);

        BankApiError error = bankApiResponse.getError();
        if (bankApiResponse.getStatus().equals("FAILED") && error != null) {
            throw new PaymentProviderException(error.getErrorMessage(), error.getErrorCode());
        }
    }

}
