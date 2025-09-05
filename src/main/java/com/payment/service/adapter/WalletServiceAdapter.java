package com.payment.service.adapter;

import com.payment.dto.payment.internal.request.ReservationRequest;
import com.payment.dto.payment.internal.response.ReservationResponse;
import com.payment.exception.InternalServiceCommunicationException;
import com.payment.exception.ReservationException;
import com.payment.model.payment.PaymentStatus;
import com.payment.service.PaymentDataAccessService;
import com.payment.service.external.WalletClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceAdapter {

    private final WalletClient walletClient;
    private final PaymentDataAccessService paymentDataAccessService;
    private final static Logger logger = LoggerFactory.getLogger(WalletServiceAdapter.class);

    public WalletServiceAdapter(WalletClient walletClient, PaymentDataAccessService paymentDataAccessService) {
        this.walletClient = walletClient;
        this.paymentDataAccessService = paymentDataAccessService;
    }

    @CircuitBreaker(name = "reserveBalance", fallbackMethod = "reserveBalanceFallback")
    public void reserveBalance(Long walletId, ReservationRequest request) {
        logger.info("[WalletServiceAdapter] - Reserving balance for walletId: {}, paymentId: {}, amount: {}",
                walletId, request.getPaymentId(), request.getRequestedAmount());
        ReservationResponse response = walletClient.reserveBalance(walletId, request);
        logger.info("[WalletServiceAdapter] - Reservation succeeded: {}", response);
    }

    public void reserveBalanceFallback(Long walletId, ReservationRequest request, Throwable throwable) {
        paymentDataAccessService
                .updatePaymentStatus(request.getPaymentId(), PaymentStatus.FAILED, throwable.getMessage());
        if (throwable instanceof ReservationException) {
            throw (ReservationException) throwable;
        }
        logger.info("[WalletServiceAdapter] - Fallback triggered for reserveBalance due to: {}", throwable.getMessage());
        throw new InternalServiceCommunicationException("Wallet service is currently unavailable.", throwable);
    }
}
