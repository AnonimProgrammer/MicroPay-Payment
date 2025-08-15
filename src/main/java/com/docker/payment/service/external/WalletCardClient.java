package com.docker.payment.service.external;

import com.docker.payment.dto.payment.external.*;
import com.docker.payment.exception.PaymentProviderException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class WalletCardClient {

    private final static Logger logger = LoggerFactory.getLogger(WalletCardClient.class);

    @CircuitBreaker(name = "withdrawCard", fallbackMethod = "fallbackWithdrawCard")
    public BankApiResponse withdrawCard(CardWithdrawalRequest cardWithdrawalRequest) {
        try {
//            ResponseEntity<BankApiResponse> response = restTemplate.exchange(
//                    BankingConfiguration.EXTERNAL_CARD_WITHDRAWAL_URL,
//                    HttpMethod.POST,
//                    new HttpEntity<>(cardWithdrawalRequest),
//                    new ParameterizedTypeReference<BankApiResponse>() {}
//            );
            // Simulating a response for demonstration purposes
            BankApiResponse apiResponse = new BankApiResponse(
                    "SUCCESS",
                    new CardOperationResponse(
                            "txn_123456",
                            "Withdrawal processed successfully.",
                            BigDecimal.valueOf(100.0),
                            "AZN"
                    ),
                    LocalDateTime.now(),
                    null
//                    new BankApiError(
//                            "INSUFFICIENT_FUNDS",
//                            "Insufficient funds in account",
//                            "BUSINESS_ERROR"
//                    )
//                    new BankApiError(
//                            "CARD_REFUSED",
//                        "Card details are invalid or expired.",
//                        "BUSINESS_ERROR"
//                    )
            );
//            throw new Exception("Simulated service error for demonstration purposes");
            return apiResponse;
        } catch (Exception exception) {
            logger.error("[WalletCardClient] - Error withdrawing card: {}.", exception.getMessage(), exception);
            throw new PaymentProviderException("Service error!", exception);
        }
    }

    @CircuitBreaker(name = "topUpCard", fallbackMethod = "fallbackTopUpCard")
    public BankApiResponse topUpCard(CardTopUpRequest cardTopUpRequest) {
        try {
//            ResponseEntity<BankApiResponse> response = restTemplate.exchange(
//                    BankingConfiguration.EXTERNAL_CARD_TOP_UP_URL,
//                    HttpMethod.POST,
//                    new HttpEntity<>(cardTopUpRequest),
//                    new ParameterizedTypeReference<BankApiResponse>() {}
//            );
            // Simulating a response for demonstration purposes
            BankApiResponse apiResponse = new BankApiResponse(
                    "SUCCESS",
//                    null,
                    new CardOperationResponse(
                            "txn_891452",
                            "Top-up processed successfully.",
                            BigDecimal.valueOf(100.0),
                            "AZN"
                    ),
                    LocalDateTime.now(),
                    null
//                    new BankApiError(
//                            "CARD_REFUSED",
//                            "Card details are invalid or expired.",
//                            "BUSINESS_ERROR"
//                    )
            );
//            throw new Exception("Simulated service error for demonstration purposes");
            return apiResponse;
        } catch (Exception exception) {
            logger.error("[WalletCardClient] - Error with card top-up: {}.", exception.getMessage(), exception);
            throw new PaymentProviderException("Service error!", exception);
        }
    }

    private BankApiResponse fallbackWithdrawCard(CardWithdrawalRequest request, Throwable throwable) {
        return fallbackCardOperation(request, throwable);
    }

    private BankApiResponse fallbackTopUpCard(CardTopUpRequest request, Throwable throwable) {
        return fallbackCardOperation(request, throwable);
    }

    private BankApiResponse fallbackCardOperation(Object request, Throwable throwable) {
        logger.error("[WalletCardClient] - Fallback triggered due to: {}", throwable.getMessage());
        BankApiResponse bankApiResponse = new BankApiResponse();
        bankApiResponse.setStatus("FAILED");
        bankApiResponse.setError(new BankApiError(
                "SERVICE_UNAVAILABLE",
                "Service is currently unavailable. Please try again later.",
                "SERVER_ERROR"
        ));
        return bankApiResponse;
    }
}
