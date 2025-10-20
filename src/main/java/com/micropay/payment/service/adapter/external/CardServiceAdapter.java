package com.micropay.payment.service.adapter.external;

import com.micropay.payment.dto.payment.external.request.CardOperationRequest;
import com.micropay.payment.dto.payment.external.response.BankApiError;
import com.micropay.payment.dto.payment.external.response.BankApiResponse;
import com.micropay.payment.dto.payment.external.response.CardOperationResponse;
import com.micropay.payment.exception.PaymentProviderException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CardServiceAdapter {

    private final static Logger logger = LoggerFactory.getLogger(CardServiceAdapter.class);

    @CircuitBreaker(name = "withdrawCard", fallbackMethod = "fallbackWithdrawCard")
    public BankApiResponse withdrawCard(CardOperationRequest cardOperationRequest) {
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
                            UUID.randomUUID().toString(),
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
    public BankApiResponse topUpCard(CardOperationRequest cardOperationRequest) {
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
                            UUID.randomUUID().toString(),
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
            logger.error("Error with card top-up: {}.", exception.getMessage(), exception);
            throw new PaymentProviderException("Service error!", exception);
        }
    }

    private BankApiResponse fallbackWithdrawCard(CardOperationRequest request, Throwable throwable) {
        return fallbackCardOperation(request, throwable);
    }

    private BankApiResponse fallbackTopUpCard(CardOperationRequest request, Throwable throwable) {
        return fallbackCardOperation(request, throwable);
    }

    private BankApiResponse fallbackCardOperation(Object request, Throwable throwable) {
        logger.error("Fallback triggered due to: {}", throwable.getMessage());
        BankApiResponse bankApiResponse = new BankApiResponse();
        bankApiResponse.setStatus("FAILED");
        bankApiResponse.setError(
                new BankApiError(
                "SERVICE_UNAVAILABLE",
                "Service is currently unavailable. Please try again later.",
                "SERVER_ERROR"
                )
        );
        return bankApiResponse;
    }
}
